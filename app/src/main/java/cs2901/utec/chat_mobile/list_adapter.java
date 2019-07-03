package cs2901.utec.chat_mobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cs2901.utec.chat_mobile.ListContact;

public class list_adapter extends RecyclerView.Adapter<list_adapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<ListContact> mExampleList;

    public list_adapter(Context context, ArrayList<ListContact> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_contact, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ListContact currentItem = mExampleList.get(position);
        String creatorName = currentItem.getCreator();
        holder.mTextViewCreator.setText(creatorName);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewCreator;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
        }
    }

}
