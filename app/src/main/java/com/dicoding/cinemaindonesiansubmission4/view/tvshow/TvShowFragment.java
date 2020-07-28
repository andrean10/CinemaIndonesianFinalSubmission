package com.dicoding.cinemaindonesiansubmission4.view.tvshow;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.cinemaindonesiansubmission4.R;
import com.dicoding.cinemaindonesiansubmission4.adapter.TvShowAdapter;
import com.dicoding.cinemaindonesiansubmission4.entity.Database;
import com.dicoding.cinemaindonesiansubmission4.viewmodel.CinemaViewModel;

import java.util.ArrayList;

public class TvShowFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String EXTRA_STATE = "extra_state";
    private ProgressBar progressBar;
    private TvShowAdapter adapter = new TvShowAdapter(this.getActivity());
    private CinemaViewModel cinemaViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView rv = view.findViewById(R.id.recyclerview);

        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        showLoading(true);

        cinemaViewModel = new ViewModelProvider(TvShowFragment.this, new ViewModelProvider.NewInstanceFactory()).get(CinemaViewModel.class);
        if (savedInstanceState == null) {
            cinemaViewModel.tvshowList(this.getActivity());
        } else {
            ArrayList<Database> listData = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (listData != null) {
                adapter.setData(listData);
            }
        }

        adapter.setOnItemClickCallBack(new TvShowAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(Database database) {
            }
        });

        cinemaViewModel.getListDatabase().observe(getViewLifecycleOwner(), new Observer<ArrayList<Database>>() {
            @Override
            public void onChanged(ArrayList<Database> database) {
                if (database != null) {
                    adapter.setData(database);
                    showLoading(false);
                } else {
                    showLoading(false);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData());
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.hintSearch));
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);

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
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            cinemaViewModel.tvshowList(this.getActivity());
        } else {
            cinemaViewModel.searchTvShowList(this.getActivity(), newText);
        }
        showLoading(true);
        return false;
    }
}