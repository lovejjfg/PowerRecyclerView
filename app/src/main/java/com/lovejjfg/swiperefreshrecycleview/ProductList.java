package com.lovejjfg.swiperefreshrecycleview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lovejjfg.powerrecycle.PowerAdapter;
import com.lovejjfg.powerrecycle.SpacesItemDecoration;
import com.lovejjfg.powerrecycle.holder.PowerHolder;
import com.lovejjfg.swiperefreshrecycleview.model.Product;
import java.util.ArrayList;

/**
 * Created by joe on 2017/9/22.
 * Email: lovejjfg@gmail.com
 */

public class ProductList extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_product);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ProductListAdapter adapter = new ProductListAdapter();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration.Builder(ViewUtils.dip2px(this, 20), 2, true).create());
        initData(adapter);
    }

    private void initData(ProductListAdapter adapter) {
        ArrayList<Product> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Product p = new Product();
            p.type = i % 2;
            p.tag = "夏季促销";
            p.title = "这应该不是是商品促销的标题";
            list.add(p);
        }
        adapter.setList(list);
    }

    static class ProductListAdapter extends PowerAdapter<Product> {
        @Override
        public PowerHolder<Product> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0) {
                return new ProductListHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_product_list1, parent, false));
            } else {
                return new ProductListHolder2(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_product_list2, parent, false));
            }
        }

        @Override
        public void onViewHolderBind(@NonNull PowerHolder<Product> holder, int position) {
            holder.onBind(list.get(position));
        }

        @Override
        public int getItemViewTypes(int position) {
            return list.get(position).type;
        }
    }

    static class ProductListHolder extends PowerHolder<Product> {

        private final TextView mTag;
        private final TextView mTitle;
        private final TextView mTitle2;

        public ProductListHolder(View itemView) {
            super(itemView);
            mTag = (TextView) itemView.findViewById(R.id.tv_tag);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTitle2 = (TextView) itemView.findViewById(R.id.tv_title2);
        }

        @Override
        public void onBind(Product product) {
            super.onBind(product);
            mTag.setText(product.tag);
            mTitle.setText(product.title);
            ViewUtils.calculateTag1(mTitle, mTitle2, product.title);
        }
    }

    static class ProductListHolder2 extends PowerHolder<Product> {

        private final TextView mTag;
        private final TextView mTitle2;

        public ProductListHolder2(View itemView) {
            super(itemView);
            mTag = (TextView) itemView.findViewById(R.id.tv_tag);
            mTitle2 = (TextView) itemView.findViewById(R.id.tv_title2);
        }

        @Override
        public void onBind(Product product) {
            super.onBind(product);
            mTag.setText(product.tag);
            mTitle2.setText(product.title);
            ViewUtils.calculateTag2(mTag, mTitle2, product.title);
        }
    }
}

