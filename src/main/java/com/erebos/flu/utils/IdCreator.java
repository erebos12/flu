package com.erebos.flu.utils;


public class IdCreator {

    public static int createKey(long accountId, FileType fileType, String year) {
        return (accountId + fileType.toString() + year).hashCode();
    }

    public static int createFileId(FileType fileType, String year) {
        return (fileType.toString() + year).hashCode();
    }
}
