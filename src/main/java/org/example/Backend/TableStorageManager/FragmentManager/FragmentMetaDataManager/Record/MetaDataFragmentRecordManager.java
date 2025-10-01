package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.Record;

import org.example.Backend.Models.MetaDataFragment;

public interface MetaDataFragmentRecordManager {
    MetaDataFragment generateMetaDataNewFragment(String nameTable, int lengthDataFragment);
}
