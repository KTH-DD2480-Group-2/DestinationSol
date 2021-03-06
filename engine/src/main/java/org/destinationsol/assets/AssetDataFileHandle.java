/*
 * Copyright 2018 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.destinationsol.assets;

import com.badlogic.gdx.files.FileHandle;
import org.terasology.gestalt.assets.format.AssetDataFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * AssetDataFileHandle is an instance of FileHandle that provides access to an AssetDataFile, rather than an actual file.
 * Only reading from files will work. Writing to files will crash the game.
 * @see com.badlogic.gdx.files.FileHandle
 */
public class AssetDataFileHandle extends FileHandle {
    protected AssetDataFile dataFile;

    public AssetDataFileHandle(AssetDataFile dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public BufferedInputStream read(int bufferSize) {
        return new BufferedInputStream(read());
    }

    @Override
    public InputStream read() {
        try {
            return dataFile.openStream();
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public String name() {
        return dataFile.getFilename();
    }

    @Override
    public String extension() {
        return dataFile.getFileExtension();
    }

    @Override
    public String nameWithoutExtension() {
        String fileName = name();
        return dataFile.getFilename().substring(0, fileName.lastIndexOf(".") + 1);
    }

    @Override
    public String path() {
        List<String> path = dataFile.getPath();

        StringBuilder builder = new StringBuilder();
        for (int segmentNo = 0; segmentNo < path.size(); segmentNo++) {
            builder.append(path.get(segmentNo));
            if (segmentNo != path.size() - 1) {
                builder.append('/');
            }
        }

        return builder.toString();
    }

    @Override
    public FileHandle parent() {
        // HACK: LibGDX's BitmapFontData uses this method to obtain a file in the same directory
        return this;
    }

    @Override
    public FileHandle child(String name) {
        // HACK: LibGDX's BitmapFontData uses this method to obtain a file in the same directory
        return new FileHandle(path() + "/" + name);
    }

    @Override
    public String pathWithoutExtension() {
        String path = path();
        return path.substring(0, path.indexOf(extension()));
    }

    @Override
    public String toString() {
        return path();
    }

    @Override
    public long length() {
        int length = -1;
        try {
            InputStream stream = dataFile.openStream();
            // HACK: This method may not produce reliable results in other JVMs.
            // It often only gives the remaining quantity of bytes in the buffer, rather than the stream.
            length = stream.available();
            stream.close();
        } catch (Exception ignore) {

        }

        return length;
    }
}
