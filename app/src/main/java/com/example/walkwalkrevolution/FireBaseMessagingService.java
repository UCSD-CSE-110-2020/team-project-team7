package com.example.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterPersonal;
import com.example.walkwalkrevolution.RecycleViewAdapters.RecyclerViewAdapterTeam;
import com.example.walkwalkrevolution.custom_data_classes.DateTimeFormatter;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.custom_data_classes.Route;
import com.example.walkwalkrevolution.forms.NotesPage;
import com.example.walkwalkrevolution.forms.SetDate;
import com.example.walkwalkrevolution.proposed_walk_observer_pattern.ProposedWalkFetcherService;
import com.example.walkwalkrevolution.proposed_walk_observer_pattern.ProposedWalkObservable;
import com.example.walkwalkrevolution.proposed_walk_observer_pattern.ProposedWalkObserver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import java.util.ArrayList;
import java.util.List;

public class FireBaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    }
}