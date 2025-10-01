package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.Models.MetaDataFragmentTableMetadata;
import org.example.Backend.TableStorageManager.FileManager.FileReader.FileReader;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.TableMetadata.MetaDataFragmentTableMetadataManager;
import org.example.Backend.Utils.ByteUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.example.Backend.TableStorageManager.FragmentManager.FragmentStructureConstants.LENGTH_INDICATOR_BYTE_COUNT;

public class FragmentTableMetadataSaver implements FragmentSaver{
    private final FileWriter fileWriter;
    private final FileReader fileReader;
    private final MetaDataFragmentTableMetadataManager metaDataFragmentTableMetadata;

    public FragmentTableMetadataSaver(FileWriter fileWriter, FileReader fileReader, MetaDataFragmentTableMetadataManager metaDataFragmentTableMetadata) {
        this.fileWriter = fileWriter;
        this.fileReader = fileReader;
        this.metaDataFragmentTableMetadata = metaDataFragmentTableMetadata;
    }

    @Override
    public int save(String tableName, List<Byte> bytes) {
        MetaDataFragmentTableMetadata metaDataFragment = metaDataFragmentTableMetadata.getMetaDataNewFragment(tableName);

        if (metaDataFragment.isContinuePreviousFragment()) {
            addLengthBytesMetadataData(bytes);
            editLengthFragment(tableName, metaDataFragment, bytes.size());
        }

        allocateBytesForLink(bytes);

        fileWriter.write(tableName, bytes, metaDataFragment.getPositionFragment());
        return 0;
    }

    private void addLengthBytesMetadataData(List<Byte> bytesData) {
        bytesData.addAll(0, intToBytes(bytesData.size()));
    }

    private List<Byte> intToBytes(int value) {
        List<Byte> byteList = new ArrayList<>();
        byteList.add((byte) (value >> 24));
        byteList.add((byte) (value >> 16));
        byteList.add((byte) (value >> 8));
        byteList.add((byte) value);
        return byteList;
    }

    private void editLengthFragment(String tableName, MetaDataFragmentTableMetadata metaDataFragment, int sizeNewFragment) {
        int oldSize = getOldSize(metaDataFragment, tableName);
        int newSize = oldSize + sizeNewFragment;

        fileWriter.write(tableName, intToBytes(newSize), metaDataFragment.getPositionLastFragment());
    }

    private int getOldSize(MetaDataFragmentTableMetadata metaDataFragment, String tableName) {
        int startPos = metaDataFragment.getPositionLastFragment();
        byte[] size = fileReader.read(tableName, startPos, LENGTH_INDICATOR_BYTE_COUNT);
        return ByteUtils.getIntFromBytes(size);
    }

    private void allocateBytesForLink(List<Byte> bytes) {
        ByteBuffer linkBytes = ByteBuffer.allocate(4);
        for (byte b : linkBytes.array()) bytes.add(b);
    }
}
