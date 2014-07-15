/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2011 Mariano Alvarez Fernandez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fgrim.msnake;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MSnake: a simple game that everyone can enjoy.
 * 
 * This is an implementation of the classic Game "Snake", in which you control a
 * serpent roaming around the garden looking for apples. Be careful, though,
 * because when you catch one, not only will you become longer, but you'll move
 * faster. Running into yourself or the walls will end the game.
 * 
 * Derived from the Snake game in the Android SDK (M.Alvarez):
 * 
 * Added touch control
 * Added view score and record
 * Added menu walls on/off
 * Added menu big,normal,small size
 * Added menu about
 * Added save preferences
 * Added vibration
 * Added red pepper, slow down the snake
 * Added records dialog
 * Added alternate input turn mode
 * Added fast speed
 * and more
 */

public class MSnake extends Activity {

//    private static final String TAG = "MSnake";

    private SnakeView mSnakeView;
    
    private static String ICICLE_KEY = "snake-view";
    
    static final int DIALOG_ABOUT_ID = 0;
    static final int DIALOG_RECORDS_ID = 1;
    static final int DIALOG_WALLS_ID = 2;
    static final int DIALOG_SIZE_ID = 3;
    static final int DIALOG_SPEED_ID = 4;
    static final int DIALOG_NEWS_ID = 5;
    static final int DIALOG_EXIT_ID = 6;
    static final int DIALOG_INPUT_ID = 7;
    static final int DIALOG_SETTINGS_ID = 8;
    static final int DIALOG_WARMSETTINGS_ID = 9;

    private RadioGroup smsrg1;
    private RadioGroup smsrg2;
    private RadioGroup smsrg3;
    private RadioGroup smsrg4;
    
    /**
     * Called when Activity is first created. Turns off the title bar, sets up
     * the content views, and fires up the SnakeView.
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       	setContentView(R.layout.snake_layout);

        mSnakeView = (SnakeView) findViewById(R.id.snake);
        mSnakeView.setTextView((TextView) findViewById(R.id.text));
        mSnakeView.setScoreView((TextView) findViewById(R.id.textscore));
        mSnakeView.setRecordView((TextView) findViewById(R.id.textrecord));

        int dHeight = getWindowManager().getDefaultDisplay().getHeight();
        int dWidth = getWindowManager().getDefaultDisplay().getWidth();
        mSnakeView.setTileSizes(dWidth, dHeight);

        // Restore preferences
        SharedPreferences settings = getPreferences(0);
        mSnakeView.restorePreferences(settings);
        setCorrectButtons();

        if (savedInstanceState == null) {
            // We were just launched -- set up a new game
            mSnakeView.setMode(SnakeView.READY);
        } else {
            // We are being restored
            Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
            if (map != null) {
                mSnakeView.restoreState(map);
            } else {
                mSnakeView.setMode(SnakeView.READY);
            }
        }
        if (mSnakeView.showNews20) showDialog(DIALOG_NEWS_ID);
//    	Log.d(TAG, "onCreate end");
    }

    protected void setCorrectButtons() {
        Button mButton[] = new Button[6];
      
        mButton[0] = (Button) findViewById(R.id.button0);
        mButton[1] = (Button) findViewById(R.id.button1);
        mButton[2] = (Button) findViewById(R.id.button2);
        mButton[3] = (Button) findViewById(R.id.button3);
        mButton[4] = (Button) findViewById(R.id.button4);
        mButton[5] = (Button) findViewById(R.id.button5);
        
        if (mSnakeView.inputMode == SnakeView.INPUT_MODE_2K) {
        	mButton[0].setVisibility(View.VISIBLE);
        	mButton[1].setVisibility(View.GONE);
        	mButton[2].setVisibility(View.GONE);
        	mButton[3].setVisibility(View.GONE);
        	mButton[4].setVisibility(View.GONE);
        	mButton[5].setVisibility(View.VISIBLE);
        } else if (mSnakeView.inputMode == SnakeView.INPUT_MODE_4K) {
        	mButton[0].setVisibility(View.GONE);
        	mButton[1].setVisibility(View.VISIBLE);
        	mButton[2].setVisibility(View.VISIBLE);
        	mButton[3].setVisibility(View.VISIBLE);
        	mButton[4].setVisibility(View.VISIBLE);
        	mButton[5].setVisibility(View.GONE);
        } else {
        	mButton[0].setVisibility(View.GONE);
        	mButton[1].setVisibility(View.GONE);
        	mButton[2].setVisibility(View.GONE);
        	mButton[3].setVisibility(View.GONE);
        	mButton[4].setVisibility(View.GONE);
        	mButton[5].setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Vibrator mvibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        mSnakeView.setVibrator(mvibrator);
        registerReceiver(receiver, new IntentFilter("myproject"));
//    	Log.d(TAG, "onResume");
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle!=null) {
				
				//extra data inserted into the fired intent
				String data = bundle.getString("data");
				Log.i("data in main class", data);
				
				
				if ("stomp".equalsIgnoreCase(data)) {
					//view.flyCow();	
				}
				
				//Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("data in main class", "bundle null");
				//Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_SHORT).show();
			}
			//handleResult(bundle);
		}

		
	};
	
    @Override
    protected void onPause() {
        super.onPause();
        // Pause the game along with the activity if RUNNING!!
        if (mSnakeView.getMode() == SnakeView.RUNNING) {
            mSnakeView.setMode(SnakeView.PAUSE);
        }
//    	Log.d(TAG, "onPause");
   }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getPreferences(0);
        mSnakeView.savePreferences(settings);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
    }
    
    public void bIzquierda(View view) {
    	mSnakeView.bIzquierda();
    }
    
    public void bArriba(View view) {
    	mSnakeView.bArriba();
    }
    
    public void bAbajo(View view) {
    	mSnakeView.bAbajo();
    }
    
    public void bDerecha(View view) {
    	mSnakeView.bDerecha();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.snake_menu, menu);
    	menu.findItem(R.id.menu_about).setIcon(
        	getResources().getDrawable(android.R.drawable.ic_menu_info_details));
    	menu.findItem(R.id.menu_records).setIcon(
            getResources().getDrawable(android.R.drawable.ic_menu_view));
    	menu.findItem(R.id.menu_settings).setIcon(
                getResources().getDrawable(android.R.drawable.ic_menu_preferences));
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	if (mSnakeView.getMode() == SnakeView.RUNNING)
            mSnakeView.setMode(SnakeView.PAUSE);
   	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_settings:
        	if (mSnakeView.getMode() == SnakeView.PAUSE)
        	    showDialog(DIALOG_WARMSETTINGS_ID);
        	else
        	    showDialog(DIALOG_SETTINGS_ID);
            return true;
        case R.id.menu_about:
        	showDialog(DIALOG_ABOUT_ID);
            return true;
        case R.id.menu_records:
        	showDialog(DIALOG_RECORDS_ID);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    void changeInputMode(int mode) {
        CharSequence str = "";
        Resources res = getResources();
        int oldMode = mSnakeView.inputMode;
        
    	if (mode == SnakeView.INPUT_MODE_2K) {
    		mSnakeView.inputMode = SnakeView.INPUT_MODE_2K;
    		str = res.getText(R.string.toast_input2k);
    		if (oldMode == SnakeView.INPUT_MODE_OG)
    			mSnakeView.firstTime = true;
    	} else if (mode == SnakeView.INPUT_MODE_OG) {
    		mSnakeView.inputMode = SnakeView.INPUT_MODE_OG;
    		str = res.getText(R.string.toast_inputog);
    		if (oldMode != SnakeView.INPUT_MODE_OG)
    			mSnakeView.firstTime = true;
    	} else {
    		mSnakeView.inputMode = SnakeView.INPUT_MODE_4K;
    		str = res.getText(R.string.toast_input4k);
    		if (oldMode == SnakeView.INPUT_MODE_OG)
    			mSnakeView.firstTime = true;
    	}
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    	setCorrectButtons();
//       mSnakeView.setMode(SnakeView.READY); ! hacer tras la llamada
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        Resources res = getBaseContext().getResources();
        AlertDialog.Builder builder;
        
        switch(id) {
        case DIALOG_SETTINGS_ID:
        	LayoutInflater inflaters = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        	View layouts = inflaters.inflate(R.layout.settings_layout,
                    (ViewGroup) findViewById(R.id.settings_layout_root));
        	AlertDialog.Builder builders = new AlertDialog.Builder(this);
        	builders.setTitle(R.string.settings_title);
        	builders.setIcon(R.drawable.redpeppertile);
        	builders.setView(layouts);
        	smsrg1 = (RadioGroup) layouts.findViewById(R.id.smsrg1);
        	smsrg2 = (RadioGroup) layouts.findViewById(R.id.smsrg2);
        	smsrg3 = (RadioGroup) layouts.findViewById(R.id.smsrg3);
        	smsrg4 = (RadioGroup) layouts.findViewById(R.id.smsrg4);
        	if (mSnakeView.noSmallSize) {
        	    RadioButton smsrg3r2 = (RadioButton) layouts.findViewById(R.id.smsrg3r2);
        	    smsrg3r2.setVisibility(View.GONE);
        	}

        	builders.setPositiveButton(R.string.dlg_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	boolean wallsOn = smsrg1.getCheckedRadioButtonId() == R.id.smsrg1r0;
                    	if (wallsOn != mSnakeView.mUseWalls)
                    		mSnakeView.changeWalls();
                    	boolean fastOn = smsrg2.getCheckedRadioButtonId() == R.id.smsrg2r1;
                    	if (fastOn != mSnakeView.mFast)
                    		mSnakeView.changeSpeed();
                    	int boardSize = SnakeView.BS_BIG;
                    	if (smsrg3.getCheckedRadioButtonId() == R.id.smsrg3r1)
                    		boardSize = SnakeView.BS_NORMAL;
                    	if (smsrg3.getCheckedRadioButtonId() == R.id.smsrg3r2)
                    		boardSize = SnakeView.BS_SMALL;
                    	if (boardSize != mSnakeView.mBoardSize)
                    		mSnakeView.zoomTileSize(boardSize);
                    	int mode = SnakeView.INPUT_MODE_4K;
                    	if (smsrg4.getCheckedRadioButtonId() == R.id.smsrg4r1)
                    		mode = SnakeView.INPUT_MODE_2K;
                    	if (smsrg4.getCheckedRadioButtonId() == R.id.smsrg4r2)
                    		mode = SnakeView.INPUT_MODE_OG;
                    	if (mode != mSnakeView.inputMode)
                    		changeInputMode(mode);
                        mSnakeView.setMode(SnakeView.READY);
                    	dialog.cancel();
                    }
                });
        	builders.setNegativeButton(R.string.dlg_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        	
        	dialog = builders.create();
        	dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            break;
        case DIALOG_ABOUT_ID:
        	LayoutInflater inflatera = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        	View layouta = inflatera.inflate(R.layout.about_layout,
                    (ViewGroup) findViewById(R.id.about_layout_root));
        	AlertDialog.Builder buildera = new AlertDialog.Builder(this);
        	buildera.setTitle(R.string.about_title);
        	buildera.setIcon(R.drawable.icon);
        	buildera.setView(layouta);

        	buildera.setPositiveButton(R.string.dlg_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	dialog.cancel();
                    }
                });
        	buildera.setNegativeButton(R.string.news_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showDialog(DIALOG_NEWS_ID);
                        dialog.cancel();
                    }
                });
        	
        	dialog = buildera.create();
        	dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            break;
        case DIALOG_NEWS_ID:
        	LayoutInflater inflatern = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        	View layoutn = inflatern.inflate(R.layout.news_layout,
                    (ViewGroup) findViewById(R.id.news_layout_root));
        	AlertDialog.Builder buildern = new AlertDialog.Builder(this);
        	buildern.setTitle(R.string.news_title);
        	buildern.setIcon(R.drawable.headeattile);
        	buildern.setView(layoutn);

        	buildern.setPositiveButton(R.string.dlg_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	mSnakeView.showNews20 = false;
                    	dialog.cancel();
                    }
                });
        	
        	dialog = buildern.create();
        	dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            break;
        case DIALOG_RECORDS_ID:
        	LayoutInflater inflaterr = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        	View layoutr = inflaterr.inflate(R.layout.records_layout,
                    (ViewGroup) findViewById(R.id.records_layout_root));
        	AlertDialog.Builder builderr = new AlertDialog.Builder(this);
        	builderr.setTitle(R.string.records_title);
        	builderr.setIcon(R.drawable.appletile);
        	builderr.setView(layoutr);

        	builderr.setPositiveButton(R.string.dlg_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	dialog.cancel();
                    }
                });
        	
        	dialog = builderr.create();
        	dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            break;
        case DIALOG_WARMSETTINGS_ID:
        	builder = new AlertDialog.Builder(this);
        	builder.setMessage(res.getString(R.string.dlg_warmsettings_title))
        	       .setCancelable(false)
        	       .setPositiveButton(res.getString(R.string.dlg_yes),
        	    	   new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   showDialog(DIALOG_SETTINGS_ID);
        	               dialog.cancel();
        	           }
        	       })
        	       .setNegativeButton(res.getString(R.string.dlg_no),
        	    	   new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
       	                   dialog.cancel();
        	           }
        	       });
        	dialog = builder.create();
        	break;
        case DIALOG_EXIT_ID:
        	builder = new AlertDialog.Builder(this);
        	builder.setMessage(res.getString(R.string.dlg_exit_title))
        	       .setCancelable(false)
        	       .setPositiveButton(res.getString(R.string.dlg_yes),
        	    	   new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   finish();
        	           }
        	       })
        	       .setNegativeButton(res.getString(R.string.dlg_no),
        	    	   new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
       	                   dialog.cancel();
        	           }
        	       });
        	dialog = builder.create();
        	break;
        default:
            dialog = null;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id) {
        case DIALOG_SETTINGS_ID:
        	if (mSnakeView.mUseWalls)
        		smsrg1.check(R.id.smsrg1r0);
        	else
        		smsrg1.check(R.id.smsrg1r1);
        	if (mSnakeView.mFast)
        		smsrg2.check(R.id.smsrg2r1);
        	else
        		smsrg2.check(R.id.smsrg2r0);
        	if (mSnakeView.mBoardSize == SnakeView.BS_BIG)
        		smsrg3.check(R.id.smsrg3r0);
        	else if (mSnakeView.mBoardSize == SnakeView.BS_NORMAL)
        		smsrg3.check(R.id.smsrg3r1);
        	else
        		smsrg3.check(R.id.smsrg3r2);
        	if (mSnakeView.inputMode == SnakeView.INPUT_MODE_4K)
        		smsrg4.check(R.id.smsrg4r0);
        	else if (mSnakeView.inputMode == SnakeView.INPUT_MODE_2K)
        		smsrg4.check(R.id.smsrg4r1);
        	else
        		smsrg4.check(R.id.smsrg4r2);
        	break;
        case DIALOG_RECORDS_ID:
            TextView mTView = (TextView) dialog.findViewById(R.id.records_layout_text);
            mTView.setText(mSnakeView.getRecordsText());
            break;
        }
    }

    @Override
    public void onBackPressed() {
    	if (mSnakeView.getMode() == SnakeView.RUNNING) {
            mSnakeView.setMode(SnakeView.PAUSE);
    	    showDialog(DIALOG_EXIT_ID);
    	} else if (mSnakeView.getMode() == SnakeView.PAUSE) {
    	    showDialog(DIALOG_EXIT_ID);
    	} else
    	    finish();
    }

}
