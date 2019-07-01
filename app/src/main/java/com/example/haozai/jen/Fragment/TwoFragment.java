package com.example.haozai.jen.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.haozai.jen.Book.Books;
import com.example.haozai.jen.Book.Booktype;
import com.example.haozai.jen.R;
import com.example.haozai.jen.Tool.BaseFragment;
import com.example.haozai.jen.Tool.BookTypeAdapter;
import com.example.haozai.jen.Tool.BooksAdapter;
import com.example.haozai.jen.Tool.all_json;
import com.example.haozai.jen.Utils.LocalSpUtil;
import com.example.haozai.jen.Utils.WebService;
import com.example.haozai.jen.buy.Buyinfo;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class TwoFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private static final String TAG = TwoFragment.class.getSimpleName();
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private SharedPreferences sp;
    private List<Books> booklist;
    String result,books,num;
    private ListView books_view;
    private ImageView logo;
    private BooksAdapter bookadapter;
    protected static final int ERROR = 1;
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        initView(view);
        isPrepared=true;
        return view;
    }
    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }
        initData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initView(View view) {
        books_view = (ListView) view.findViewById(R.id.books_view);
        logo = (ImageView) view.findViewById(R.id.load_zhen);
    }

    private void initData() {
          bookadapter = new BooksAdapter(getActivity());
        //从本地获取登录信息
        String readstring = LocalSpUtil.getLogining(getActivity());
        if (!readstring.equals("success")) {
            Toast.makeText(getActivity(), "请登录...", Toast.LENGTH_LONG).show();
        }
        // 创建子线程
        new Thread(new MyThread()).start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    // 子线程接收数据，主线程修改数据
    public class MyThread implements Runnable {
        @Override
        public void run() {
            try {
                result = WebService.booktype();
                if (result==null) {
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putString("result", result);
                            Message meg = new Message();
                            meg.setData(bundle);
                            findHandler.sendMessage(meg);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = ERROR;
                failHandler.sendMessage(msg);
            }

        }
    }


    Handler booksHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case -1:
            Toast.makeText(getActivity(),"服务器连接失败!", Toast.LENGTH_SHORT).show();
                    break;
            }

            Bundle bundle;
            bundle = msg.getData();
            String resul = bundle.getString("books");
             booklist = all_json.jsonbookList(resul);
            books_view.setAdapter(bookadapter);
            bookadapter.setBooksList(booklist);

             books_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Books book =booklist.get(i);
                    num=book.getBooknum();
                    new Thread(){

                        public void run () {
                           // books = WebService.booksinfo(num);
                            try {
                                //创建意图对象
                                    Intent intent = new Intent(getActivity(),Buyinfo.class);
                                    //设置传递键值对
                                   intent.putExtra("data",num);
                                    //激活意图
                                    startActivity(intent);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        };

                    }.start();
                }
            });
        }
    };


    Handler failHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ERROR:
                    Toast.makeText(getActivity(), "连接服务器失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    Handler findHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle;
            bundle = msg.getData();
            String result = bundle.getString("result");
            final List<Booktype> list = all_json.jsonBkType(result);

            BookTypeAdapter adapter = new BookTypeAdapter(getActivity(), R.layout.bookname, list);
            ListView listView = (ListView) getView().findViewById(R.id.booklist_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (logo.getVisibility()==VISIBLE){
                        logo.setVisibility(GONE);
                    }
                    Booktype book =list.get(i);
                    Toast.makeText(getActivity(), book.getType(), Toast.LENGTH_LONG).show();
                    num=book.getNumber();
                    new Thread(){
                        public void run () {
                            Message message = new Message();
                            try {
                                books = WebService.books(num);
                                if (books==null) {
                                    Toast.makeText(getActivity(), books, Toast.LENGTH_LONG).show();
                                }
                                //q获取number
                                Bundle bundle = new Bundle();
                                bundle.putString("books", books);
                                message.setData(bundle);
                                booksHandler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                                //使用-1代表程序异常
                                message.what=ERROR;
                                message.obj=e;
                                failHandler.sendMessage(message);
                            }

                        };

                    }.start();

                }
            });

        }
    };

}

