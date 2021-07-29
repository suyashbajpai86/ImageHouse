package com.example.imagehouse.ui;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Ravi Rathore on 08/02/19.
 */
public abstract class EndlessRecyclerWithHeaderOnScrollListener extends
        RecyclerView.OnScrollListener {

    public static String TAG = EndlessRecyclerWithHeaderOnScrollListener.class
            .getSimpleName();
    private boolean loading;
    public WrapContentLinearLayoutManager mLinearLayoutManager;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount = mLinearLayoutManager.getItemCount();
        int visibleItemCount = mLinearLayoutManager.getChildCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

        // The minimum amount of items to have below your current scroll position before loading more.
        int visibleThreshold = 3;
        if (!loading && firstVisibleItem >= 0 && totalItemCount > visibleThreshold &&
                (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached
            loading = true;
            onLoadMore();
        }
    }

    protected abstract void onLoadMore();

    public void setLoading() {
        loading = false;
    }

    public void setLoaded(){
        loading = true;
    }
}
