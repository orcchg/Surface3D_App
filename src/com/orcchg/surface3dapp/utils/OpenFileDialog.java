/**
 * Copyright (c) 2015, Alov Maxim <alovmax@yandex.ru>
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.orcchg.surface3dapp.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.orcchg.surface3dapp.R;

public class OpenFileDialog extends Dialog implements OnClickListener {
  private static final String TAG = "OFileD";

  private static final String FILE_KEY = "filename";
  private static final String IMAGE_KEY = "fileimage";

  private File currentDir = new File("/");
  private ListView view = null;
  private FilenameFilter filter = null;
  private OnFileSelectedListener onFileSelectedListener = null;

  public OpenFileDialog(Context context, String dir, String[] fileExt, OnFileSelectedListener listener) {
    super(context);
    init(dir, fileExt, listener);
  }

  @Override
  public void onClick(View v) {
    browseUp();
  }

  
  /* Private methods */
  // --------------------------------------------------------------------------
  private void init(String dir, String[] fileExt, OnFileSelectedListener listener) {
    onFileSelectedListener = listener;

    if (dir != null && new File(dir).exists()) {
      currentDir = new File(dir);
    }

    prepareFileFilter(fileExt);

    setContentView(R.layout.ofd_layout);
    setTitle(R.string.ofd_title);

    view = (ListView) findViewById(R.id.ofd_list);
    browseTo(currentDir);

    view.setOnItemClickListener(new OnItemClickListener() {
      @SuppressWarnings("unchecked")
      public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        Map<String, String> listitem = (Map<String, String>) a.getItemAtPosition(position);
        String text = listitem.get(FILE_KEY);
        File file = new File(currentDir.getAbsolutePath() + File.separator + text);
        if (!browseTo(file) && onFileSelectedListener != null) {
          onFileSelectedListener.onFileSelected(file);
          dismiss();
        }
      }
    });

    Button upButton = (Button) findViewById(R.id.ofd_go_up);
    upButton.setOnClickListener(this);
  }

  private void prepareFileFilter(final String[] ext) {
    if (ext == null) {
      return;
    }

    filter = new FilenameFilter() {
      public boolean accept(File dir, String filename) {
        if (new File(dir + File.separator + filename).isDirectory()) {
          return true;
        }
        for (String e : ext) {
          if (filename.endsWith(e)) {
            return true;
          }
        }
        return false;
      }
    };
  }

  private boolean browseTo(File dir) {
    if (!dir.isDirectory()) {
      return false;
    } else {
      fillListView(dir);
      currentDir = dir;
      TextView pathView = (TextView) findViewById(R.id.ofd_current_path);
      pathView.setText(currentDir.getAbsolutePath());
      return true;
    }
  }

  private void browseUp() {
    if (currentDir.getParentFile() != null) {
      browseTo(currentDir.getParentFile());
    }
  }

  private void fillListView(File dir) {
    List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
    String[] files = filter != null ? dir.list(filter) : dir.list();
    for (String file : files) {
      Map<String, Object> item = new HashMap<String, Object>();
      item.put(FILE_KEY, file);
      int imageid = new File(dir.getAbsolutePath() + File.separator + file).isDirectory() ? R.drawable.dialogdir : R.drawable.dialogfile;
      item.put(IMAGE_KEY, imageid);
      list.add(item);
    }

    SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.ofd_list_item, new String[] {FILE_KEY, IMAGE_KEY}, new int[] {R.id.ofd_item_text, R.id.ofd_item_image});
    view.setAdapter(adapter);
  }

}
