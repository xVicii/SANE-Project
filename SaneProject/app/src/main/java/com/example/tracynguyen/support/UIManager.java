package com.example.tracynguyen.support;

import android.app.Activity;
import android.content.Context;
import android.media.ImageReader;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tracynguyen.network.AdjacencyTableEntry;
import com.example.tracynguyen.network.LL1Daemon;
import com.example.tracynguyen.network.LL2Daemon;
import com.example.tracynguyen.network.LL2P;
import com.example.tracynguyen.network.LL3P;
import com.example.tracynguyen.network.LRPDaemon;
import com.example.tracynguyen.network.Messenger;
import com.example.tracynguyen.network.RouteTable;
import com.example.tracynguyen.network.RouteTableEntry;
import com.example.tracynguyen.network.Scheduler;
import com.example.tracynguyen.network.TestTables;
import com.example.tracynguyen.saneproject.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tracy.nguyen on 1/21/2016.
 */
public class UIManager {
    private Activity parentActivity;
    private Context context;
    private Factory myFactory;
    private LL1Daemon LL1_Daemon;
    private LL2Daemon LL2_Daemon;
    private LRPDaemon lrpDaemon;
    private TestTables testTables;
    private Messenger messenger;

    /*Screen Widgets*/
    private TextView LL2PDestAddressTextView;
    private TextView LL2PSrcAddressTextView;
    private TextView LL2PTypeFieldTextView;
    private TextView LL2PCRCTextView;
    private TextView LL2PPayloadTextView;

    /*Adjacency Table Widgets*/
    private ListView AdjacencyTableListView;
    private Button AdjacencyTableAddButton;
    private Button AdjacencyTableClearButton;
    private EditText AdjacencyTableLL2PAddressTextEdit;
    private EditText AdjacencyTableIPAddressTextEdit;
    private List<AdjacencyTableEntry> AdjacencyTableList;
    private ArrayAdapter AdjacencyTableArrayAdapter;
    private EditText AdjacencyTablePayloadTextEdit;

    /*Routing and Forwarding Table Widgets*/
    private ListView RoutingTableListView;
    private List<RouteTableEntry> RoutingTableList;
    private ArrayAdapter RoutingTableArrayAdapter;
    private ListView ForwardingTableListView;
    private List<RouteTableEntry> ForwardingTableList;
    private ArrayAdapter ForwardingTableArrayAdapter;

    /*LL3P Screen Widgets*/
    private TextView LL3PDestAddressTextView;
    private TextView LL3PSrcAddressTextView;
    private TextView LL3PTypeFieldTextView;
    private TextView LL3PIDTextView;
    private TextView LL3PPayloadTextView;

    public UIManager(){
        //messenger = new Messenger(parentActivity);
    }

    public void getObjectReferences(Factory factory){
        myFactory = factory;
        parentActivity = factory.getParentActivity();
        context = parentActivity.getBaseContext();
        LL1_Daemon = factory.getLL1_Daemon();
        AdjacencyTableList = LL1_Daemon.getAdjacencyList();
        LL2_Daemon = factory.getLL2_Daemon();
        //testTables = factory.getTestTables();

        lrpDaemon = factory.getLrpDaemon();
        RoutingTableList = lrpDaemon.getRoutingTableAsList();
        ForwardingTableList = lrpDaemon.getForwardingTableAsList();
        messenger = new Messenger(parentActivity);
        messenger.getObjectReferences(myFactory);
        //initWidgets();
    }

    public void raiseToast(String message, int length){
        Toast.makeText(context, message, length).show();
    }

    public void raiseToast(String message){
        raiseToast(message, Toast.LENGTH_SHORT);
    }

    public void initWidgets(){

        /*LL2P Widgets*/
        LL2PDestAddressTextView = (TextView) parentActivity.findViewById(R.id.LL2PDestAddressTextView);
        LL2PSrcAddressTextView = (TextView) parentActivity.findViewById(R.id.LL2PSrcAddressTextView);
        LL2PTypeFieldTextView = (TextView) parentActivity.findViewById(R.id.LL2PTypeFieldTextView);
        LL2PCRCTextView = (TextView) parentActivity.findViewById(R.id.LL2PCRCTextView);
        LL2PPayloadTextView = (TextView) parentActivity.findViewById(R.id.LL2PPayloadTextView);

        /*LL3P Widgets*/
        LL3PDestAddressTextView = (TextView) parentActivity.findViewById(R.id.LL3PDestAddressTextView);
        LL3PSrcAddressTextView = (TextView) parentActivity.findViewById(R.id.LL3PSrcAddressTextView);
        LL3PTypeFieldTextView = (TextView) parentActivity.findViewById(R.id.LL3PTypeFieldTextView);
        LL3PIDTextView = (TextView) parentActivity.findViewById(R.id.LL3PIDFieldTextView);
        LL3PPayloadTextView = (TextView) parentActivity.findViewById(R.id.LL3PPayloadTextView);

        /*Adjacency Table Widgets*/
        AdjacencyTableLL2PAddressTextEdit = (EditText) parentActivity.findViewById(R.id.AdjacencyTableLL2PAddressTextEdit);
        AdjacencyTableIPAddressTextEdit = (EditText) parentActivity.findViewById(R.id.AdjacencyTableIPAddressTextEdit);
        AdjacencyTablePayloadTextEdit = (EditText) parentActivity.findViewById(R.id.AdjacencyTablePayloadTextEdit);
        AdjacencyTableListView = (ListView) parentActivity.findViewById(R.id.AdjacencyTableListView);
        AdjacencyTableAddButton = (Button) parentActivity.findViewById(R.id.AdjacencyTableAddButton);
        AdjacencyTableClearButton = (Button) parentActivity.findViewById(R.id.AdjacencyTableClearButton);

        AdjacencyTableAddButton.setOnClickListener(addAdjacency);
        AdjacencyTableClearButton.setOnClickListener(clearLL2PAndIPTextEditFields);
        AdjacencyTableListView.setOnItemClickListener(sendToLL2P);
        AdjacencyTableListView.setOnItemLongClickListener(removeAdjacencyListItem);

        // connects list array with the adapter and formats it for the screen
        AdjacencyTableArrayAdapter = new ArrayAdapter<AdjacencyTableEntry>(parentActivity,
                android.R.layout.simple_list_item_1,
                AdjacencyTableList);

        resetAdjacencyListAdaptor();
        // connects adapter with the widget on the screen
        AdjacencyTableListView.setAdapter(AdjacencyTableArrayAdapter);

        /*Routing and Forwarding Table Widgets*/
        RoutingTableListView = (ListView) parentActivity.findViewById(R.id.RoutingTableListView);
        ForwardingTableListView = (ListView) parentActivity.findViewById(R.id.ForwardingTableListView);

        /*Routing Table Connection*/
        RoutingTableArrayAdapter = new ArrayAdapter<RouteTableEntry>(parentActivity,
                android.R.layout.simple_list_item_1,
                RoutingTableList);

        resetRoutingTableListAdapter();
        RoutingTableListView.setAdapter(RoutingTableArrayAdapter);

        /*Forwarding Table Connection*/
        ForwardingTableArrayAdapter = new ArrayAdapter<RouteTableEntry>(parentActivity,
                android.R.layout.simple_list_item_1,
                ForwardingTableList);

        resetForwardingTableListAdapter();
        ForwardingTableListView.setAdapter(ForwardingTableArrayAdapter);

    }

    public void updateLL2PDisplay(LL2P frame){
        LL2PDestAddressTextView.setText(frame.getDestMACAddressHexString().toUpperCase());
        LL2PSrcAddressTextView.setText(frame.getSrcMACAddressHexString().toUpperCase());
        LL2PTypeFieldTextView.setText(frame.getTypeFieldHexString().toUpperCase());
        LL2PCRCTextView.setText(frame.getCRCHexString().toUpperCase());
        LL2PPayloadTextView.setText(frame.getPayloadHexString());
    }

    public View.OnClickListener addAdjacency = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LL1_Daemon.setAdjacency(Integer.valueOf(AdjacencyTableLL2PAddressTextEdit.getText().toString(),16),
                    AdjacencyTableIPAddressTextEdit.getText().toString());
            // ???
            LL2_Daemon.sendARPUpdate(Integer.valueOf(AdjacencyTableLL2PAddressTextEdit.getText().toString(),16));
            resetAdjacencyListAdaptor();
        }
    };

    public View.OnClickListener clearLL2PAndIPTextEditFields = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AdjacencyTableLL2PAddressTextEdit.setText("");
            AdjacencyTableIPAddressTextEdit.setText("");
            AdjacencyTablePayloadTextEdit.setText("");
        }
    };

    private AdapterView.OnItemClickListener sendToLL2P = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AdjacencyTableEntry target = AdjacencyTableList.get(position);

            LL2_Daemon.sendLL2PEchoRequest(AdjacencyTablePayloadTextEdit.getText().toString(),
                    target.getLL2PAddress());

            /*
            // create a fake frame
            LL2P newFrame = new LL2P(target.getLL2PAddressHexString(),
                    NetworkConstants.MY_LL2P_ADDRESS,
                    NetworkConstants.LL2P_ECHO_REQUEST,
                    AdjacencyTablePayloadTextEdit.getText().toString());


            // transmit the frame
            LL1_Daemon.sendLL2PFrame(newFrame);
            */
        }
    };

    private AdapterView.OnItemLongClickListener removeAdjacencyListItem = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            AdjacencyTableEntry target = AdjacencyTableList.get(position);

            LL1_Daemon.removeAdjacency(target.getLL2PAddress());
            resetAdjacencyListAdaptor();
            return false;
        }
    };

    private void resetAdjacencyListAdaptor(){
        // get the current list
        AdjacencyTableList = LL1_Daemon.getAdjacencyList();

        // clear the adjacency list
        AdjacencyTableArrayAdapter.clear();

        // load the list items in the adapter and update the screen
        Iterator<AdjacencyTableEntry> listIterator = AdjacencyTableList.iterator();
        while (listIterator.hasNext())
            AdjacencyTableArrayAdapter.add(listIterator.next());
    }

    public void resetRoutingTableListAdapter(){
        RoutingTableList = lrpDaemon.getRoutingTableAsList();

        RoutingTableArrayAdapter.clear();

        Iterator<RouteTableEntry> listIterator = RoutingTableList.iterator();
        while (listIterator.hasNext())
            RoutingTableArrayAdapter.add(listIterator.next());
    }

    public void resetForwardingTableListAdapter(){
        ForwardingTableList = lrpDaemon.getForwardingTableAsList();

        ForwardingTableArrayAdapter.clear();

        Iterator<RouteTableEntry> listIterator = ForwardingTableList.iterator();
        while (listIterator.hasNext())
            ForwardingTableArrayAdapter.add(listIterator.next());
    }

    public void updateLL3PDisplay(LL3P frame){
        LL3PDestAddressTextView.setText(frame.getDestLL3PAddressHexString().toUpperCase());
        LL3PSrcAddressTextView.setText(frame.getSrcLL3PAddressHexString().toUpperCase());
        LL3PTypeFieldTextView.setText(frame.getTypeFieldHexString().toUpperCase());
        LL3PIDTextView.setText(frame.getIDHexString().toUpperCase());
        LL3PPayloadTextView.setText(frame.getPayloadHexString());
    }

    public void openMessengerWindow(){
        messenger.openMessengerWindow();
    }

    public void receiveMessage(Integer sourceLL3P, String text){
        messenger.receiveMessage(sourceLL3P, text);
    }
}
