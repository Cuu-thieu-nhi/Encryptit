package com.example.encryptit.mInterface;


import com.example.encryptit.model.TempImageToView;

import java.util.List;

public interface IClickImageListener {
    void onClickImageListener(TempImageToView encryptedImage);
    void onLongClickImageListener(List<TempImageToView> encryptedImages);
}
