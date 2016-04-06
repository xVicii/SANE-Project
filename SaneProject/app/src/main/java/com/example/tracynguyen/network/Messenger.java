package com.example.tracynguyen.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.example.tracynguyen.network.ForwardingTable;
import com.example.tracynguyen.network.LL3PDaemon;
import com.example.tracynguyen.network.RouteTableEntry;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tracynguyen.saneproject.R;
import com.example.tracynguyen.support.Factory;
import com.example.tracynguyen.support.SoundPlayer;
import com.example.tracynguyen.support.UIManager;

/**
 * This is a stand-alone window class you can import into your project.  Steps:
 * 1. instantiate this class as an object in your UIManager. You should do this in the 
 *    UIManager's constructor method.  Just pass the activity down to this object.
 * 2. Change your UIManager to call this class's getObjectReferences method here from 
 *       UIManager's getObjectReferences() method. 
 * 	     Pass the factory to this getObjectreferences() method from the UIManager's getObjectReferences method.
 * 3. This method uses the following router classes:
 * 		UIManager - the raiseToast is used.  This can be deleted if you prefer.
 *      The getObjectReferences calls the RoutingDaemon's getFIB method to get the reference for the forwarding table.
 *      The layer3Daemon's sendPayloadToLL3PDestination(ll3Paddress, payloadBytes) method is called.
 * 4. Change the import statements at the top of the class from "moticon.networks.xxx" to your own package names.
 * 5. Remove or comment out ALL references to the soundPlayer object. This is a utility that plays sounds. If you want 
 *      one I'll be happy to provide it, but you'll probably have to do some editing to get it to work on your
 *      system. 
 * 
 * WARNINGS: If you have any typos or differently spelled classes/names from the labs you'll have to edit this 
 *    class to match what you have. For example, if you use LL3PDemon instead of LL3PDaemon, you'll have to change 
 *    it here.  
 *    
 *    You'll also have to ensure all the methods called by this class exist and return the correct object to calls from 
 *    this class.
 *    
 *    FINALLY: If you don't like the window size, feel free to edit it (line 119 as of this documentation).
 *      
 *          
 *   
 *         
 * 
 * @author pat.smith
 *
 */
public class Messenger implements Runnable {
	private Activity parentActivity;				      // reference for parent activity
	private ArrayList<String> names;					  // array list of student names. HARDCODED!
	private UIManager uiManager;						  // reference for ui manager
	private LL3PDaemon layer3Daemon;					  // reference for layer 3 daemon
	private ForwardingTable FIB;						  // reference for forwarding table
	private List<RouteTableEntry> validNetworkList;		  // the route list containing current valid remote networks.
	
	private ArrayList<NameAndNetwork> allNamesAndNetworks;// an array list with hardcoded student and network numbers
	private ArrayList<NameAndNetwork> spinnerNames;		  // names on the spinner. allows user to select a person
	
	private Spinner nameListSpinner;					  // is the spinner widget for the window
	private ArrayAdapter<NameAndNetwork> spinnerAdapter ; // contains names for the spinner
	private Button exitButton;							  // exit the chat window
	private Button sendTextButton;						  // button to send text
	private EditText messageEditText;					  // edit text where user enters text to send.
	
	private LayoutInflater inflater; 					  // inflater used to inflate window
	//private PopupWindow messengerWindow;				  // window object. contains other widgets
	AlertDialog messengerWindow;						  // 
	private View messengerView;							  // a view object to access widgets in the window
	private View messengerLayout;						  // the layout object for the entire layout file
	
	//  There are two string arrays because I use one for holding what's been received
	//  and another for what's on the screen. It allows me to add to the 'received' array without
	//  double loading when I reset the adapter.  
	private List<ChatMessage> onScreenMessagesList;		  // an object which holds the strings on the screen
	private List<ChatMessage> receivedMessagelist;		  // an array to hold the messages received from remote senders
	private ListView receivedMessagesListView;			  // the list view object which is on the screen.
	private MessageAdapter messageAdapter;				  // the custom adapter to build and display screen objects.
	
	private ScheduledThreadPoolExecutor nameWatcher;			  // manages thread to watch the spinner and keep it up to date
	private SoundPlayer soundPlayer;
	/**
	 * Constructor. 
	 * @param activity - pass the parent activity that contains context and UI elements.
	 */
	public Messenger(Activity activity){
		parentActivity = activity;
		names = new ArrayList<String>();	// a fixed array of strings with names.
		spinnerNames = new ArrayList<NameAndNetwork>();	// an array of objects which match a network to a student name
		receivedMessagelist = new ArrayList<ChatMessage>();  // a list of received messages. Each message contains the name and message.
		addNames(); // build the names array, and then populate the network/name array as well.
	}
	
	/**
	 * Standard getObjectReferences() method. This asks the factory for objects which this class needs references to.
	 * @param factory
	 */
	public void getObjectReferences(Factory factory){
		uiManager = factory.getUiManager();
		FIB = factory.getLrpDaemon().getFIB(); // forwarding information base, used to populate the active names list.
		layer3Daemon = factory.getLl3PDaemon(); // layer 3 daemon, used to send messages.
		
		// go ahead and create the window object. If you don't do this now then things
		//  can blow up if a message arrives and the window object doesn't exist yet.
		inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		messengerLayout = inflater.inflate(R.layout.messenger_layout,
				               (ViewGroup) parentActivity.findViewById(R.id.messengerLinearLayout));
		//messengerWindow = new PopupWindow(messengerLayout, 440, 350, true);
		AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
		builder.setTitle("Instant Messenger");
		builder.setCancelable(false);
		builder.setView(messengerLayout);
		messengerWindow = builder.create();
		//soundPlayer = factory.getSoundPlayer();
	}
	
	/**
	 * this is called when the UIManager is asking to open this window. It inflates the window and sets everything into action.
	 */
	public void openMessengerWindow(){
		messengerWindow.show();// messengerWindow.(messengerLayout, Gravity.NO_GRAVITY, 10, 50);
		messengerView = messengerLayout;
		// set up the spinner object.  
		nameListSpinner = (Spinner) messengerView.findViewById(R.id.nameSpinner);
		spinnerAdapter = new ArrayAdapter<NameAndNetwork>(parentActivity.getBaseContext(), 
				android.R.layout.simple_spinner_dropdown_item, spinnerNames);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nameListSpinner.setAdapter(spinnerAdapter);
		
		// set up the buttons and the edit text widget.
		exitButton = (Button) messengerView.findViewById(R.id.messengerExitButton);
		sendTextButton = (Button) messengerView.findViewById(R.id.messengerSendButton);
		exitButton.setOnClickListener(exitMessengerWindow);
		sendTextButton.setOnClickListener(sendMessage);
		messageEditText = (EditText) messengerView.findViewById(R.id.messengerEditText);
		
		// start a thread that updates the spinner every second. This thread reads the FIB and uses
		//   the FIB to know which networks are readable. These are then used as indexes into the spinner to
		//   show students actively in the network.
		nameWatcher = new ScheduledThreadPoolExecutor(3); // create the schedule object, allow for 2 threads.
		nameWatcher.scheduleAtFixedRate(this, /* object's run() method, this could point to any object Runnable method.*/
								 5, /* time to wait for router to boot*/
								 1, /*seconds betweeen runs - check for new names every second*/
								 TimeUnit.SECONDS);
		
		// set up the list view objects.  A list of messages on the screen, the list of messages received, the ListView, and adapter.
		onScreenMessagesList = new ArrayList<ChatMessage>();
		receivedMessagelist.clear();
		receivedMessagesListView = (ListView) messengerView.findViewById(R.id.recievedMessagesListView);
		messageAdapter = new MessageAdapter(parentActivity, (ArrayList<ChatMessage>) onScreenMessagesList);
		receivedMessagesListView.setAdapter(messageAdapter);
		// these next two attributes of the LIstView cause it to scroll from the bottom. A nice feature for a chat window.
		receivedMessagesListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		receivedMessagesListView.setStackFromBottom(true);
		resetSpinner();
	}

	/**
	 * This is called in response to the user pressing the Send button. 
	 *  Get the edit text contents and the spinner position and send the message using the layer 3 daemon.
	 */
	private View.OnClickListener sendMessage = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Integer spinnerPosition = nameListSpinner.getSelectedItemPosition(); // position in spinner
			if (spinnerPosition < 0){ // if the spinner position is < 0, then the spinner is either empty or not selected.
				return;
			}
			/*
			 * use the spinner position as an index into the spinner adapter to get the network from that 
			 * spinner item (each spinner item has both a name and network number).
			 */
			Integer networkNumber = spinnerAdapter.getItem(spinnerPosition).getNetwork();  
			// pull the network number out of the ll3p address of the name selected by the spinner.

			Integer destinationLL3P = networkNumber * 256 + 1; // all user ll3p addreses are, in this lab = 1*net + 1 (eg 1.1 = 17, 256*1+1)

			/*
			 * This just defaults to "me" for the sender, you can change this to whatever you want!
			 */
			addMessageAndSource("Me", messageEditText.getText().toString());
			// send the message and reset the edit text window.
			
			layer3Daemon.sendPayloadToLL3PDestination(destinationLL3P, messageEditText.getText().toString().getBytes());
			messageEditText.setText("");
			soundPlayer.playSendMessageSound();
		}
	};
	
	/**
	 * Receive a message. It is expected that the UI Manager will call this method when a message is 
	 *    to be posted on the screen.   The LL3P Daemon will pass the message to the UI Manager, whicih
	 *    then passes the message and LL3P address here.  -- the Ll3P daemon could call this directly,
	 *    but that breaks the model a little bit.
	 * @param ll3pAddress
	 * @param payload
	 */
	public void receiveMessage(Integer ll3pAddress, String payload){
		if (messengerWindow.isShowing()){
			int network = (Integer) (ll3pAddress/256); // network portion = address/256 with no remainder.
			
													   // note that in original "256" comes from network constants class.
			if (network >= 0 && network <16){
				String name = new String(allNamesAndNetworks.get(network-1).name);
				addMessageAndSource(name,payload);
				soundPlayer.playReceiveMessageSound();
			} else {
				uiManager.raiseToast("Network out of Bounds in Chat. Network = "+Integer.toString(network) +
											", message was: "+payload);
			}
		}
		else {
			uiManager.raiseToast("chat window not open, message received");
			soundPlayer.playAlertSound();
		}
	}
	
	/**
	 * Add a message to the message window and update the adapter so it's displayed onscreen.
	 * @param name - user name to display
	 * @param message - message text as a string
	 */
	private void addMessageAndSource(String name, String message){
		receivedMessagelist.add(new ChatMessage(name, message));
		resetMessageAdapter();
	}
	
	/**
	 * reset the message adapter so it's current with existing messages received.
	 */
	private void resetMessageAdapter(){
		messageAdapter.clear(); // clear the adapter
		// now loop through the received message list array and add all these messages to the adapter.
		for (ChatMessage message : receivedMessagelist){
			messageAdapter.add(message);
		}
		receivedMessagesListView.setAdapter(messageAdapter);
	}

	/**
	 * close the messenger app cleanly.
	 */
	private View.OnClickListener exitMessengerWindow = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			nameWatcher.shutdown(); 			// stop the thread task
			//nameListAdapter.clear();			
			messengerWindow.dismiss();			// close the window.
		}
	};

	private void addNames(){
		names.add("Drew");
		names.add("Hacker #2");
		names.add("Mambo #3");
		names.add("The Donald");
		names.add("Allison");
		names.add("Cody");
		names.add("The Marshall");
		names.add("Preston");
		names.add("Mitch");
		names.add("Matthew");
		names.add("Silas");
		names.add("Corbin");
		names.add("Tracy (Love guru)");
		names.add("NSA ^&h.==?");
		names.add("Moticon");
		allNamesAndNetworks = new ArrayList<NameAndNetwork>();
		int i=0;
		for (String name : names){
			allNamesAndNetworks.add(new NameAndNetwork(name, i+1));
			i++;
		}
	}
	
	/**
	 * the name and network class is a private class used to create an object
	 * where the name/network pairs can be stored and used as a single object
	 * by the spinner.
	 * @author pat
	 *
	 */
	private class NameAndNetwork{
		private String name;		// user name
		private Integer network;	// network for this user.
		
		NameAndNetwork(String nm, Integer net){
			name = nm;
			network = net;
		}

		public Integer getNetwork() {
			return network;
		}

		@Override
		public String toString() {
			return name + " (net = " + network+ ")";
		}
	}

	/**
	 * this little run method is here to update the spinner with new adjacencies from the route table.
	 * it is called by the schedule pool executor. It runs every second when the messenger window is open.
	 */
	@Override
	public void run() {
		parentActivity.runOnUiThread(new Runnable() { 
			public void run() {
				resetSpinner();
			}
		});
	}
	
	/**
	 * reset spinner is called by the threaded run() method above and any time the system 
	 * needs to make sure the spinner object has all the known networks represented in the screen
	 * spinner.
	 * It's very simple: get the route list from the forwarding table, clear and reload all of these
	 * networks and their associated names into the spinner object.
	 */
	private void resetSpinner(){
		validNetworkList = FIB.getRouteList(); // get active nodes from forwarding information table.
		spinnerNames.clear(); // clear the spinner names
		// reload the spinner names from scratch so as to include any new networks that came on line since the last time this was run.
		for (RouteTableEntry entry : validNetworkList){
			if (entry.getNetDistPair().getNetworkNumber().compareTo(-1) > 0 && entry.getNetDistPair().getNetworkNumber().compareTo(16) < 0)
					spinnerAdapter.add(allNamesAndNetworks.get(entry.getNetDistPair().getNetworkNumber()-1)); // network numbers go from 1-15, array elements from 0-14
		}
		
	}
	
	/**
	 * The message adapter is a custom adapter for showing the messages.
	 */
	private class MessageAdapter extends ArrayAdapter<ChatMessage> {

		public MessageAdapter(Context parentActivity,  ArrayList<ChatMessage> nameAndMessages) {
			// this is called when the adapter is created.  The person creating
			// the adapter provides the activity and the strings. The layout file is hardcoded here.
			super(parentActivity, R.layout.chat_layout, nameAndMessages);
		}
		
		/**
		 * the get view class is called when the UI (the android UI, not UIManager)
		 * needs to redraw or add rows or delete rows. 
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			//create a rowholder object to use... 
			MessageHolder messageHolder =null;
			// make a local copy of my row to be converted. 
			View rowView = convertView;
			// If the row doesn't exist inflate a new rowView, create a new rowholder object
			//  and attach the rowholder to this row view object
			if (rowView == null){
				// get the inflater system service object., inflate the row, attach it to the textview, grab the tag.
				LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parentActivity);
				rowView = inflater.inflate(R.layout.chat_layout, parent, false);
				messageHolder = new MessageHolder(rowView);
				rowView.setTag(messageHolder);
			} else {
				// use the get_tag method of the rowView object to retrieve the rowHolder with the widgets and text.
				messageHolder = (MessageHolder) rowView.getTag(R.id.messageTextView);
			}
			// put the string in the screen view object.
			messageHolder.populateRow(this.getItem(position));
			return rowView;
		}
	}
	
	/**
	 * this static class simply provides us with a handy way to get the ID's of the
	 * widget types defined in the XML system one time when we create each row.
	 */
    private static class MessageHolder {
		TextView message;
		TextView name;
		
		// create the route row holder by getting the Ids of the xml widgets.
		MessageHolder(View row){
			message = (TextView) row.findViewById(R.id.messageTextView);
			name = (TextView) row.findViewById(R.id.senderTextView);
		}
		// given a route table entry, push the items in the route table into the text view objects.
		public void populateRow(ChatMessage entry){
			message.setText(entry.message);
			name.setText(entry.name);
		}
	}
    
    /**
     * the ChatMessage class provides a class for storing the message and associated name. 
     * very little goes on here.
     * @author pat
     *
     */
    private class ChatMessage{
    	String name;
    	String message;
    	
    	ChatMessage(String nm, String msg){
    		name = nm;
    		message = msg;
    	}
    }
}