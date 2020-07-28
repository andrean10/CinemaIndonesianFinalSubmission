package com.dicoding.cinemalistfavorite.view.favorite;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.cinemalistfavorite.R;
import com.dicoding.cinemalistfavorite.adapter.TvShowAdapter;
import com.dicoding.cinemalistfavorite.entity.Database;
import com.dicoding.cinemalistfavorite.mapping.MappingHelper;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.dicoding.cinemalistfavorite.db.DatabaseContract.CONTENT_URI_TVSHOW;

public class FavoriteTvShowFragment extends Fragment implements LoadFavoriteTvShowCallback {

    private static final String EXTRA_STATE = "extra_state";
    private ProgressBar progressBar;
    private final TvShowAdapter adapter = new TvShowAdapter(getActivity());
    private RecyclerView rv;
    private DataObserver myObserver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        rv = view.findViewById(R.id.recyclerview);
        setHasOptionsMenu(true);

        // registerContentObserver
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        myObserver = new DataObserver(handler, getActivity(), this);
        getActivity().getContentResolver().registerContentObserver(CONTENT_URI_TVSHOW, true, myObserver);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);

        adapter.setOnItemClickCallBack(new TvShowAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(Database database) {
            }
        });

        if (savedInstanceState == null) {
            new LoadTvShowAsync(getActivity(), this).execute();
        } else {
            ArrayList<Database> listFavorite = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (listFavorite != null) {
                adapter.setData(listFavorite);
            }
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData());
    }

    @Override
    public void preExecute() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoading(true);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Database> database) {
        showLoading(false);
        if (database.size() > 0) {
            adapter.setData(database);
        } else {
            adapter.setData(new ArrayList<Database>());
            showSnackbarMessage(getResources().getString(R.string.empty_data_tvshow));
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rv, message, Snackbar.LENGTH_SHORT).show();
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private static class LoadTvShowAsync extends AsyncTask<Void, Void, ArrayList<Database>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteTvShowCallback> weakCallback;

        LoadTvShowAsync(Context context, LoadFavoriteTvShowCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Database> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(
                    CONTENT_URI_TVSHOW,
                    null,
                    null,
                    null,
                    null
            );
            assert dataCursor != null;
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Database> database) {
            super.onPostExecute(database);
            weakCallback.get().postExecute(database);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        final LoadFavoriteTvShowCallback callback;

        DataObserver(Handler handler, Context context, LoadFavoriteTvShowCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadTvShowAsync(context, callback).execute();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.change_language) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Objects.requireNonNull(getActivity()).getContentResolver().unregisterContentObserver(myObserver);
    }
}

interface LoadFavoriteTvShowCallback {
    void preExecute();

    void postExecute(ArrayList<Database> database);
}