package cytech.android.todolist;

import static cytech.android.todolist.Backup.exportDB;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    DatabaseAdapter databaseAdapter;
    ListView taskView;
    FloatingActionButton btnAdd;
    SimpleCursorAdapter adapter;
    Runnable updateView;
    SharedPreferences sp;
    Button btnImport, btnExport;

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // handle action bar button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnEditName) {
            updateName(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Shared Preferences
        sp = getApplicationContext().getSharedPreferences("mypref" , Context.MODE_PRIVATE);
        //Get name from Shared Preferences
        String name = sp.getString("MY_NAME", "John Doe");
        this.setTitle(name + "'s jobs");
        // Float button
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(v);
            }
        });
        // List view
        taskView = findViewById(R.id.taskView);
        databaseAdapter = new DatabaseAdapter(this);
        adapter = databaseAdapter.populateListViewFromDB();
        taskView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateView = new Runnable() {
            public void run() {
                //reload content
                adapter.changeCursor(databaseAdapter.reloadCursor());
                adapter.notifyDataSetChanged();
                taskView.invalidateViews();
                taskView.refreshDrawableState();
            }
        };
        taskView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteItem(view, (int) id);

                return false;
            }
        });
        // Export
        btnExport = findViewById(R.id.btnExport);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getRootView().getContext();
                exportDB(context);
            }
        });
        // Import
        btnImport = findViewById(R.id.btnImport);
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getRootView().getContext();
//                importDB(context);
                Toast.makeText(context, "IMPORTED", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateName(MainActivity activity) {
        Context context =  findViewById(R.id.taskView).getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View formElementsView = inflater.inflate(R.layout.add_form, null, false);
        EditText editTxt = formElementsView.findViewById(R.id.editText);

        //Create a dialog
        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("New Name: ")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                // Update new name
                                String name = editTxt.getText().toString();
                                activity.setTitle(name + "'s jobs");
                                // Save to Shared Preferences
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("MY_NAME", name);
                                editor.commit();
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void addItem(View view) {
        Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View formElementsView = inflater.inflate(R.layout.add_form, null, false);
        EditText editTxt = formElementsView.findViewById(R.id.editText);

        //Create a dialog
        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("New Task: ")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                // Add new task to database
                                boolean createSuccessful = new DatabaseAdapter(context).create(editTxt.getText().toString());
                                if(createSuccessful){
                                    Toast.makeText(context, "New task was added successfully.", Toast.LENGTH_SHORT).show();
                                    runOnUiThread(updateView);
                                }else{
                                    Toast.makeText(context, "Unable to add new task.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteItem(View view, int id){
        Context context = view.getRootView().getContext();
        //Create a dialog
        new AlertDialog.Builder(context)
                .setTitle("Confirm")
                .setMessage("Do you want to delete this task?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Delete task
                        boolean deleteSuccessful = new DatabaseAdapter(context).delete(id);
                        if(deleteSuccessful){
                            Toast.makeText(context, "Task was deleted successfully.", Toast.LENGTH_SHORT).show();
                            runOnUiThread(updateView);
                        }else{
                            Toast.makeText(context, "Unable to delete task.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                })
                .show();
    }
}