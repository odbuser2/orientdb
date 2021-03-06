package com.orientechnologies.orient.core.index.hashindex.local;

import com.orientechnologies.common.directmemory.ODirectMemoryPointer;
import com.orientechnologies.common.serialization.types.OByteSerializer;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.index.hashindex.local.cache.OCacheEntry;
import com.orientechnologies.orient.core.storage.impl.local.paginated.base.ODurablePage;

import java.io.IOException;

/**
 * @author Andrey Lomakin <a href="mailto:lomakin.andrey@gmail.com">Andrey Lomakin</a>
 * @since 5/14/14
 */
public class ODirectoryPage extends ODurablePage {
  private static final int  ITEMS_OFFSET   = NEXT_FREE_POSITION;

  public static final int   NODES_PER_PAGE = (OGlobalConfiguration.DISK_CACHE_PAGE_SIZE.getValueAsInteger() * 1024 - ITEMS_OFFSET)
                                               / OHashTableDirectory.BINARY_LEVEL_SIZE;

  private final OCacheEntry entry;

  public ODirectoryPage(ODirectMemoryPointer pagePointer, TrackMode trackMode, OCacheEntry entry) {
    super(pagePointer, trackMode);
    this.entry = entry;
  }

  public OCacheEntry getEntry() {
    return entry;
  }

  public void setMaxLeftChildDepth(int localNodeIndex, byte maxLeftChildDepth) {
    int offset = getItemsOffset() + localNodeIndex * OHashTableDirectory.BINARY_LEVEL_SIZE;
    setByteValue(offset, maxLeftChildDepth);
  }

  public byte getMaxLeftChildDepth(int localNodeIndex) {
    int offset = getItemsOffset() + localNodeIndex * OHashTableDirectory.BINARY_LEVEL_SIZE;
    return getByteValue(offset);
  }

  public void setMaxRightChildDepth(int localNodeIndex, byte maxRightChildDepth) {
    int offset = getItemsOffset() + localNodeIndex * OHashTableDirectory.BINARY_LEVEL_SIZE + OByteSerializer.BYTE_SIZE;
    setByteValue(offset, maxRightChildDepth);
  }

  public byte getMaxRightChildDepth(int localNodeIndex) {
    int offset = getItemsOffset() + localNodeIndex * OHashTableDirectory.BINARY_LEVEL_SIZE + OByteSerializer.BYTE_SIZE;
    return getByteValue(offset);
  }

  public void setNodeLocalDepth(int localNodeIndex, byte nodeLocalDepth) {
    int offset = getItemsOffset() + localNodeIndex * OHashTableDirectory.BINARY_LEVEL_SIZE + 2 * OByteSerializer.BYTE_SIZE;
    setByteValue(offset, nodeLocalDepth);
  }

  public byte getNodeLocalDepth(int localNodeIndex) {
    int offset = getItemsOffset() + localNodeIndex * OHashTableDirectory.BINARY_LEVEL_SIZE + 2 * OByteSerializer.BYTE_SIZE;
    return getByteValue(offset);
  }

  public void setPointer(int localNodeIndex, int index, long pointer) throws IOException {
    int offset = getItemsOffset() + (localNodeIndex * OHashTableDirectory.BINARY_LEVEL_SIZE + 3 * OByteSerializer.BYTE_SIZE)
        + index * OHashTableDirectory.ITEM_SIZE;

    setLongValue(offset, pointer);
  }

  public long getPointer(int localNodeIndex, int index) {
    int offset = getItemsOffset() + (localNodeIndex * OHashTableDirectory.BINARY_LEVEL_SIZE + 3 * OByteSerializer.BYTE_SIZE)
        + index * OHashTableDirectory.ITEM_SIZE;

    return getLongValue(offset);
  }

  protected int getItemsOffset() {
    return ITEMS_OFFSET;
  }
}
