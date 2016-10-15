package com.example.hayatomoritani.weatherapp.DragAndDrop;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hayatomoritani.weatherapp.R;

public class DragAndDropMain extends AppCompatActivity {

    private LinearLayout newContainer;

    private TextView dragText;

    private LinearLayout dragContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_drop_main);
        getContainer();
        getDraggable();
    }

    void getContainer(){
        newContainer = (LinearLayout)findViewById(R.id.newContainer);
        newContainer.setOnDragListener(new ChoiceDragListener());
    }

    void getDraggable(){
        dragText = (TextView)findViewById(R.id.draggingObj);
        dragContainer = (LinearLayout)findViewById(R.id.draggingContainer);

        dragText.setOnTouchListener(new ChoiceTouchListener());
        dragContainer.setOnDragListener(new ChoiceDragListener());
    }

    private final class ChoiceTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                ClipData data = ClipData.newPlainText("","");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data,shadowBuilder,view,0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else return false;
        }
    }

    private class ChoiceDragListener implements View.OnDragListener{

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            View draggedV = (View)dragEvent.getLocalState();
            ViewGroup parentSourceV = (ViewGroup)draggedV.getParent();
            LinearLayout container = (LinearLayout)view;

            switch (dragEvent.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    draggedV.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DROP:
                    if (container.getChildCount() <1){
                        swapItems(parentSourceV,draggedV,container,2.0f);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    void swapItems(ViewGroup parentSourceV,View draggedV,LinearLayout container, float scale){
        parentSourceV.removeView(draggedV);
        container.addView(draggedV);
        draggedV.setPivotX(0);
        draggedV.setPivotY(0);
        draggedV.setScaleX(scale);
        draggedV.setScaleY(scale);

        draggedV.setVisibility(View.VISIBLE);
    }

}
