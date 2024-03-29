package com.techtraining.cosechasapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.User;
import com.techtraining.cosechasapp.db.UserDao;

public class InsertUpdateUser extends AsyncTask<Void, Void, Void> {

    private int id;
    private String name;
    private String email;
    private Context context;
    public InsertUpdateUser(Context context, int id, String name, String email) {
        this.context = context;
        this.id  = id;
        this.name = name;
        this.email = email;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase appDatabase = DBManager.getInstance(context);
        UserDao userDao = appDatabase.userDao();
        User user = userDao.loadById(id);
        if (user == null) {
            user = new User();
            user.id = id;
            user.name = name;
            user.email = email;
            userDao.insertOne(user);
        }
        else {
            user.name = name;
            user.email = email;
            userDao.update(user);
        }
        return null;
    }
}
