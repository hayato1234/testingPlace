package com.example.hayatomoritani.weatherapp.GoogleSuggest;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hayatomoritani.weatherapp.MainActivity;
import com.example.hayatomoritani.weatherapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public class Suggests extends AppCompatActivity {

    private EditText origText;
    private ListView suggList;
    private TextView ebandText;

    private Handler guiThread;
    private ExecutorService suggThread;
    private Runnable updateTask;
    private Future<?> suggPending;
    private List<String> items;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggests);
        initThreading();
        findViews();
        setListeners();
        setAdapters();
    }

    /** Get a handle to all user interface elements */
    private void findViews() {
        origText = (EditText) findViewById(R.id.original_text);
        suggList = (ListView) findViewById(R.id.result_list);
        ebandText = (TextView) findViewById(R.id.eband_text);
    }

    /** Set up adapter for list view. */
    private void setAdapters() {
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        suggList.setAdapter(adapter);
    }

    /** Setup user interface event handlers */
    private void setListeners() {
        // Define listener for text change
        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            /* Do nothing */
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                queueUpdate(1000 /* milliseconds */);
            }
            public void afterTextChanged(Editable s) {
            /* Do nothing */
            }
        };

        // Set listener on the original text field
        origText.addTextChangedListener(textWatcher);

        // Define listener for clicking on an item
        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String query = (String) parent.getItemAtPosition(position);
                doSearch(query);
            }
        };

        // Set listener on the suggestion list
        suggList.setOnItemClickListener(clickListener);

        // Make website link clickable
        ebandText.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private void doSearch(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);
    }
    /**
     * Initialize multi-threading. There are two threads: 1) The main
     * graphical user interface thread already started by Android,
     * and 2) The suggest thread, which we start using an executor.
     */
    private void initThreading() {
        guiThread = new Handler();
        suggThread = Executors.newSingleThreadExecutor();

        // This task gets suggestions and updates the screen
        updateTask = new Runnable() {
            public void run() {
                // Get text to suggest
                String original = origText.getText().toString().trim();

                // Cancel previous suggestion if there was one
                if (suggPending != null)
                    suggPending.cancel(true);

                // Check to make sure there is text to work on
                if (original.length() != 0) {
                    // Let user know we're doing something
                    setText(R.string.working);

                    // Begin suggestion now but don't wait for it
                    try {
                        SuggestTask suggestTask = new SuggestTask(
                                Suggests.this, // reference to activity
                                original // original text
                        );
                        suggPending = suggThread.submit(suggestTask);
                    } catch (RejectedExecutionException e) {
                        // Unable to start new task
                        setText(R.string.error);
                    }
                }
            }
        };
    }
    /** Request an update to start after a short delay */
    private void queueUpdate(long delayMillis) {
        // Cancel previous update if it hasn't started yet
        guiThread.removeCallbacks(updateTask);
        // Start an update if nothing happens after a few milliseconds
        guiThread.postDelayed(updateTask, delayMillis);
    }

    /** Modify list on the screen (called from another thread) */
    public void setSuggestions(List<String> suggestions) {
        guiSetList(suggList, suggestions);
    }

    /** All changes to the GUI must be done in the GUI thread */
    private void guiSetList(final ListView view,
                            final List<String> list) {
        guiThread.post(new Runnable() {
            public void run() {
                setList(list);
            }

        });
    }

    /** Display a message */
    private void setText(int id) {
        adapter.clear();
        adapter.add(getResources().getString(id));
    }

    /** Display a list */
    private void setList(List<String> list) {
        adapter.clear();
        adapter.addAll(list);
    }

}
