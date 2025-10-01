package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.Models.MetaDataFragmentTableMetadata;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProviderImpl;
import org.example.Backend.TableStorageManager.FileManager.FileReader.FileReader;
import org.example.Backend.TableStorageManager.FileManager.FileReader.FileReaderImpl;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriterImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.TableMetadata.MetaDataFragmentTableMetadataManager;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FragmentTableMetadataSaverTest {
    private final FilePathProvider filePathProvider = new FilePathProviderImpl();
    private final FileWriter fileWriter = new FileWriterImpl(filePathProvider);
    private final FileReader fileReader = new FileReaderImpl(filePathProvider);
    private final String NAME_TABLE = "test_table";
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(filePathProvider);

    private final int lengthBytesData = 10;
    private final int positionFirstFragment = 5;

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        FragmentTableMetadataSaver fragmentTableMetadataSaver = getFragmentSaver();
        List<Byte> testData = generateTestDataForSave();
        fragmentTableMetadataSaver.save(NAME_TABLE, testData);
    }

    private FragmentTableMetadataSaver getFragmentSaver() {
        MetaDataFragmentTableMetadataManager metaDataFragmentTableMetadataManager = mockMetaDataFragmentManager();
        return new FragmentTableMetadataSaver(fileWriter, fileReader, metaDataFragmentTableMetadataManager);
    }

    private MetaDataFragmentTableMetadataManager mockMetaDataFragmentManager() {
        MetaDataFragmentTableMetadata metadataFirstFragment = new MetaDataFragmentTableMetadata(positionFirstFragment, false, 0);


        MetaDataFragmentTableMetadataManager metaDataFragmentTableMetadataManager = mock(MetaDataFragmentTableMetadataManager.class);
        when(metaDataFragmentTableMetadataManager.getMetaDataNewFragment(NAME_TABLE)).thenReturn(metadataFirstFragment);
        return metaDataFragmentTableMetadataManager;
    }

    private List<Byte> generateTestDataForSave() {
        List<Byte> bytesData = new ArrayList<>();

        for (int i = 0; i < lengthBytesData; i++) {
            Random random = new Random();
            bytesData.add((byte) (random.nextInt(50) + 1));
        }
        return bytesData;
    }
}