package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import java.io.File;

public interface IUploadService {

    public ServerResponse uploadFile(File uploadFile);

}
