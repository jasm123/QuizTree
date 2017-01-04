package com.example.user.quiztree.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    public WidgetService() {
    }


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("WidgetService", "Reached in service");
        return new WidgetDataProvider(this.getApplicationContext(), intent);
    }
}
