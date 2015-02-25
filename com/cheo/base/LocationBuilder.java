package com.cheo.base;

import com.cheo.base.enums.FileType;

public interface LocationBuilder {

	String buildPath(FileType type) throws Exception;
}
