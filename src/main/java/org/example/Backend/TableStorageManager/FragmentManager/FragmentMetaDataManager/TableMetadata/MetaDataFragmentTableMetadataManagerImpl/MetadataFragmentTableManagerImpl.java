package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.TableMetadata.MetaDataFragmentTableMetadataManagerImpl;

import org.example.Backend.Models.MetaDataFragmentTableMetadata;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.TableMetadata.MetaDataFragmentTableMetadataManager;

public class MetadataFragmentTableManagerImpl implements MetaDataFragmentTableMetadataManager {
    private final CreatorMetadataFragment creatorNewFragment;
    private final EditorMetadataFragment editorMetadataFragment;

    public MetadataFragmentTableManagerImpl(CreatorMetadataFragment creatorNewFragment, EditorMetadataFragment editorMetadataFragment) {
        this.creatorNewFragment = creatorNewFragment;
        this.editorMetadataFragment = editorMetadataFragment;
    }

    @Override
    public MetaDataFragmentTableMetadata getMetaDataNewFragment(String nameTable) {
        MetaDataFragmentTableMetadata metaDataFragmentTableMetadata = creatorNewFragment.createNewFragment(nameTable);
        editorMetadataFragment.editAfterCreateNewFragment(nameTable);
        return metaDataFragmentTableMetadata;
    }
}
