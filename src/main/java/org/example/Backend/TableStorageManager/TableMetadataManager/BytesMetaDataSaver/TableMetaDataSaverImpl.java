package org.example.Backend.TableStorageManager.TableMetadataManager.BytesMetaDataSaver;

import org.example.Backend.DataToBytesConverter.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TablePartType;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;

import java.util.ArrayList;

public class TableMetaDataSaverImpl {
    private final FragmentSaver fragmentSaver;

    public TableMetaDataSaverImpl(FragmentSaver fragmentSaver) {
        this.fragmentSaver = fragmentSaver;
    }

    public int save(String tableName, TableMetaData tableMetaData) {
        TablePartTypeConverter<TableMetaData> tabularDataConverter =  BytesConverterFactory.getTablePartTypeConverter(TablePartType.METADATA);
        ArrayList<Byte> bytesData = tabularDataConverter.toBytes(tableMetaData);
        return fragmentSaver.save(tableName, bytesData);
    }

}
