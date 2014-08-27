/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package com.xiaomi.miui.ad.thrift.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.thrift.*;
import org.apache.thrift.async.*;
import org.apache.thrift.meta_data.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.protocol.*;

public class MiuiAdStoreServiceLogBiddingEffectRecord implements TBase<MiuiAdStoreServiceLogBiddingEffectRecord, MiuiAdStoreServiceLogBiddingEffectRecord._Fields>, java.io.Serializable, Cloneable {
  private static final TStruct STRUCT_DESC = new TStruct("MiuiAdStoreServiceLogBiddingEffectRecord");

  private static final TField SCRIBE_INFO_FIELD_DESC = new TField("scribeInfo", TType.STRUCT, (short)1);
  private static final TField LOG_TYPE_FIELD_DESC = new TField("logType", TType.STRING, (short)2);
  private static final TField PACKAGE_NAME_FIELD_DESC = new TField("packageName", TType.STRING, (short)3);
  private static final TField DOWNLOAD_NO_FIELD_DESC = new TField("downloadNo", TType.I64, (short)4);
  private static final TField PRICE_FIELD_DESC = new TField("price", TType.I64, (short)5);
  private static final TField CONSUMPTION_FIELD_DESC = new TField("consumption", TType.I64, (short)6);

  private MiuiLogScribeInfo scribeInfo;
  private String logType;
  private String packageName;
  private long downloadNo;
  private long price;
  private long consumption;

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements TFieldIdEnum {
    SCRIBE_INFO((short)1, "scribeInfo"),
    LOG_TYPE((short)2, "logType"),
    PACKAGE_NAME((short)3, "packageName"),
    DOWNLOAD_NO((short)4, "downloadNo"),
    PRICE((short)5, "price"),
    CONSUMPTION((short)6, "consumption");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // SCRIBE_INFO
          return SCRIBE_INFO;
        case 2: // LOG_TYPE
          return LOG_TYPE;
        case 3: // PACKAGE_NAME
          return PACKAGE_NAME;
        case 4: // DOWNLOAD_NO
          return DOWNLOAD_NO;
        case 5: // PRICE
          return PRICE;
        case 6: // CONSUMPTION
          return CONSUMPTION;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __DOWNLOADNO_ISSET_ID = 0;
  private static final int __PRICE_ISSET_ID = 1;
  private static final int __CONSUMPTION_ISSET_ID = 2;
  private BitSet __isset_bit_vector = new BitSet(3);

  public static final Map<_Fields, FieldMetaData> metaDataMap;
  static {
    Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SCRIBE_INFO, new FieldMetaData("scribeInfo", TFieldRequirementType.DEFAULT, 
        new StructMetaData(TType.STRUCT, MiuiLogScribeInfo.class)));
    tmpMap.put(_Fields.LOG_TYPE, new FieldMetaData("logType", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.STRING)));
    tmpMap.put(_Fields.PACKAGE_NAME, new FieldMetaData("packageName", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.STRING)));
    tmpMap.put(_Fields.DOWNLOAD_NO, new FieldMetaData("downloadNo", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.I64)));
    tmpMap.put(_Fields.PRICE, new FieldMetaData("price", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.I64)));
    tmpMap.put(_Fields.CONSUMPTION, new FieldMetaData("consumption", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.I64)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    FieldMetaData.addStructMetaDataMap(MiuiAdStoreServiceLogBiddingEffectRecord.class, metaDataMap);
  }

  public MiuiAdStoreServiceLogBiddingEffectRecord() {
  }

  public MiuiAdStoreServiceLogBiddingEffectRecord(
    MiuiLogScribeInfo scribeInfo,
    String logType,
    String packageName,
    long downloadNo,
    long price,
    long consumption)
  {
    this();
    this.scribeInfo = scribeInfo;
    this.logType = logType;
    this.packageName = packageName;
    this.downloadNo = downloadNo;
    setDownloadNoIsSet(true);
    this.price = price;
    setPriceIsSet(true);
    this.consumption = consumption;
    setConsumptionIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MiuiAdStoreServiceLogBiddingEffectRecord(MiuiAdStoreServiceLogBiddingEffectRecord other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    if (other.isSetScribeInfo()) {
      this.scribeInfo = new MiuiLogScribeInfo(other.scribeInfo);
    }
    if (other.isSetLogType()) {
      this.logType = other.logType;
    }
    if (other.isSetPackageName()) {
      this.packageName = other.packageName;
    }
    this.downloadNo = other.downloadNo;
    this.price = other.price;
    this.consumption = other.consumption;
  }

  public MiuiAdStoreServiceLogBiddingEffectRecord deepCopy() {
    return new MiuiAdStoreServiceLogBiddingEffectRecord(this);
  }

  @Override
  public void clear() {
    this.scribeInfo = null;
    this.logType = null;
    this.packageName = null;
    setDownloadNoIsSet(false);
    this.downloadNo = 0;
    setPriceIsSet(false);
    this.price = 0;
    setConsumptionIsSet(false);
    this.consumption = 0;
  }

  public MiuiLogScribeInfo getScribeInfo() {
    return this.scribeInfo;
  }

  public void setScribeInfo(MiuiLogScribeInfo scribeInfo) {
    this.scribeInfo = scribeInfo;
  }

  public void unsetScribeInfo() {
    this.scribeInfo = null;
  }

  /** Returns true if field scribeInfo is set (has been asigned a value) and false otherwise */
  public boolean isSetScribeInfo() {
    return this.scribeInfo != null;
  }

  public void setScribeInfoIsSet(boolean value) {
    if (!value) {
      this.scribeInfo = null;
    }
  }

  public String getLogType() {
    return this.logType;
  }

  public void setLogType(String logType) {
    this.logType = logType;
  }

  public void unsetLogType() {
    this.logType = null;
  }

  /** Returns true if field logType is set (has been asigned a value) and false otherwise */
  public boolean isSetLogType() {
    return this.logType != null;
  }

  public void setLogTypeIsSet(boolean value) {
    if (!value) {
      this.logType = null;
    }
  }

  public String getPackageName() {
    return this.packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public void unsetPackageName() {
    this.packageName = null;
  }

  /** Returns true if field packageName is set (has been asigned a value) and false otherwise */
  public boolean isSetPackageName() {
    return this.packageName != null;
  }

  public void setPackageNameIsSet(boolean value) {
    if (!value) {
      this.packageName = null;
    }
  }

  public long getDownloadNo() {
    return this.downloadNo;
  }

  public void setDownloadNo(long downloadNo) {
    this.downloadNo = downloadNo;
    setDownloadNoIsSet(true);
  }

  public void unsetDownloadNo() {
    __isset_bit_vector.clear(__DOWNLOADNO_ISSET_ID);
  }

  /** Returns true if field downloadNo is set (has been asigned a value) and false otherwise */
  public boolean isSetDownloadNo() {
    return __isset_bit_vector.get(__DOWNLOADNO_ISSET_ID);
  }

  public void setDownloadNoIsSet(boolean value) {
    __isset_bit_vector.set(__DOWNLOADNO_ISSET_ID, value);
  }

  public long getPrice() {
    return this.price;
  }

  public void setPrice(long price) {
    this.price = price;
    setPriceIsSet(true);
  }

  public void unsetPrice() {
    __isset_bit_vector.clear(__PRICE_ISSET_ID);
  }

  /** Returns true if field price is set (has been asigned a value) and false otherwise */
  public boolean isSetPrice() {
    return __isset_bit_vector.get(__PRICE_ISSET_ID);
  }

  public void setPriceIsSet(boolean value) {
    __isset_bit_vector.set(__PRICE_ISSET_ID, value);
  }

  public long getConsumption() {
    return this.consumption;
  }

  public void setConsumption(long consumption) {
    this.consumption = consumption;
    setConsumptionIsSet(true);
  }

  public void unsetConsumption() {
    __isset_bit_vector.clear(__CONSUMPTION_ISSET_ID);
  }

  /** Returns true if field consumption is set (has been asigned a value) and false otherwise */
  public boolean isSetConsumption() {
    return __isset_bit_vector.get(__CONSUMPTION_ISSET_ID);
  }

  public void setConsumptionIsSet(boolean value) {
    __isset_bit_vector.set(__CONSUMPTION_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case SCRIBE_INFO:
      if (value == null) {
        unsetScribeInfo();
      } else {
        setScribeInfo((MiuiLogScribeInfo)value);
      }
      break;

    case LOG_TYPE:
      if (value == null) {
        unsetLogType();
      } else {
        setLogType((String)value);
      }
      break;

    case PACKAGE_NAME:
      if (value == null) {
        unsetPackageName();
      } else {
        setPackageName((String)value);
      }
      break;

    case DOWNLOAD_NO:
      if (value == null) {
        unsetDownloadNo();
      } else {
        setDownloadNo((Long)value);
      }
      break;

    case PRICE:
      if (value == null) {
        unsetPrice();
      } else {
        setPrice((Long)value);
      }
      break;

    case CONSUMPTION:
      if (value == null) {
        unsetConsumption();
      } else {
        setConsumption((Long)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case SCRIBE_INFO:
      return getScribeInfo();

    case LOG_TYPE:
      return getLogType();

    case PACKAGE_NAME:
      return getPackageName();

    case DOWNLOAD_NO:
      return new Long(getDownloadNo());

    case PRICE:
      return new Long(getPrice());

    case CONSUMPTION:
      return new Long(getConsumption());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case SCRIBE_INFO:
      return isSetScribeInfo();
    case LOG_TYPE:
      return isSetLogType();
    case PACKAGE_NAME:
      return isSetPackageName();
    case DOWNLOAD_NO:
      return isSetDownloadNo();
    case PRICE:
      return isSetPrice();
    case CONSUMPTION:
      return isSetConsumption();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof MiuiAdStoreServiceLogBiddingEffectRecord)
      return this.equals((MiuiAdStoreServiceLogBiddingEffectRecord)that);
    return false;
  }

  public boolean equals(MiuiAdStoreServiceLogBiddingEffectRecord that) {
    if (that == null)
      return false;

    boolean this_present_scribeInfo = true && this.isSetScribeInfo();
    boolean that_present_scribeInfo = true && that.isSetScribeInfo();
    if (this_present_scribeInfo || that_present_scribeInfo) {
      if (!(this_present_scribeInfo && that_present_scribeInfo))
        return false;
      if (!this.scribeInfo.equals(that.scribeInfo))
        return false;
    }

    boolean this_present_logType = true && this.isSetLogType();
    boolean that_present_logType = true && that.isSetLogType();
    if (this_present_logType || that_present_logType) {
      if (!(this_present_logType && that_present_logType))
        return false;
      if (!this.logType.equals(that.logType))
        return false;
    }

    boolean this_present_packageName = true && this.isSetPackageName();
    boolean that_present_packageName = true && that.isSetPackageName();
    if (this_present_packageName || that_present_packageName) {
      if (!(this_present_packageName && that_present_packageName))
        return false;
      if (!this.packageName.equals(that.packageName))
        return false;
    }

    boolean this_present_downloadNo = true;
    boolean that_present_downloadNo = true;
    if (this_present_downloadNo || that_present_downloadNo) {
      if (!(this_present_downloadNo && that_present_downloadNo))
        return false;
      if (this.downloadNo != that.downloadNo)
        return false;
    }

    boolean this_present_price = true;
    boolean that_present_price = true;
    if (this_present_price || that_present_price) {
      if (!(this_present_price && that_present_price))
        return false;
      if (this.price != that.price)
        return false;
    }

    boolean this_present_consumption = true;
    boolean that_present_consumption = true;
    if (this_present_consumption || that_present_consumption) {
      if (!(this_present_consumption && that_present_consumption))
        return false;
      if (this.consumption != that.consumption)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_scribeInfo = true && (isSetScribeInfo());
    builder.append(present_scribeInfo);
    if (present_scribeInfo)
      builder.append(scribeInfo);

    boolean present_logType = true && (isSetLogType());
    builder.append(present_logType);
    if (present_logType)
      builder.append(logType);

    boolean present_packageName = true && (isSetPackageName());
    builder.append(present_packageName);
    if (present_packageName)
      builder.append(packageName);

    boolean present_downloadNo = true;
    builder.append(present_downloadNo);
    if (present_downloadNo)
      builder.append(downloadNo);

    boolean present_price = true;
    builder.append(present_price);
    if (present_price)
      builder.append(price);

    boolean present_consumption = true;
    builder.append(present_consumption);
    if (present_consumption)
      builder.append(consumption);

    return builder.toHashCode();
  }

  public int compareTo(MiuiAdStoreServiceLogBiddingEffectRecord other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    MiuiAdStoreServiceLogBiddingEffectRecord typedOther = (MiuiAdStoreServiceLogBiddingEffectRecord)other;

    lastComparison = Boolean.valueOf(isSetScribeInfo()).compareTo(typedOther.isSetScribeInfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetScribeInfo()) {
      lastComparison = TBaseHelper.compareTo(this.scribeInfo, typedOther.scribeInfo);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetLogType()).compareTo(typedOther.isSetLogType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLogType()) {
      lastComparison = TBaseHelper.compareTo(this.logType, typedOther.logType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPackageName()).compareTo(typedOther.isSetPackageName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPackageName()) {
      lastComparison = TBaseHelper.compareTo(this.packageName, typedOther.packageName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDownloadNo()).compareTo(typedOther.isSetDownloadNo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDownloadNo()) {
      lastComparison = TBaseHelper.compareTo(this.downloadNo, typedOther.downloadNo);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPrice()).compareTo(typedOther.isSetPrice());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPrice()) {
      lastComparison = TBaseHelper.compareTo(this.price, typedOther.price);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetConsumption()).compareTo(typedOther.isSetConsumption());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetConsumption()) {
      lastComparison = TBaseHelper.compareTo(this.consumption, typedOther.consumption);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(TProtocol iprot) throws TException {
    TField field;
    iprot.readStructBegin();
    while (true)
    {
      field = iprot.readFieldBegin();
      if (field.type == TType.STOP) { 
        break;
      }
      switch (field.id) {
        case 1: // SCRIBE_INFO
          if (field.type == TType.STRUCT) {
            this.scribeInfo = new MiuiLogScribeInfo();
            this.scribeInfo.read(iprot);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 2: // LOG_TYPE
          if (field.type == TType.STRING) {
            this.logType = iprot.readString();
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 3: // PACKAGE_NAME
          if (field.type == TType.STRING) {
            this.packageName = iprot.readString();
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 4: // DOWNLOAD_NO
          if (field.type == TType.I64) {
            this.downloadNo = iprot.readI64();
            setDownloadNoIsSet(true);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 5: // PRICE
          if (field.type == TType.I64) {
            this.price = iprot.readI64();
            setPriceIsSet(true);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 6: // CONSUMPTION
          if (field.type == TType.I64) {
            this.consumption = iprot.readI64();
            setConsumptionIsSet(true);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        default:
          TProtocolUtil.skip(iprot, field.type);
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();
    validate();
  }

  public void write(TProtocol oprot) throws TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    if (this.scribeInfo != null) {
      oprot.writeFieldBegin(SCRIBE_INFO_FIELD_DESC);
      this.scribeInfo.write(oprot);
      oprot.writeFieldEnd();
    }
    if (this.logType != null) {
      oprot.writeFieldBegin(LOG_TYPE_FIELD_DESC);
      oprot.writeString(this.logType);
      oprot.writeFieldEnd();
    }
    if (this.packageName != null) {
      oprot.writeFieldBegin(PACKAGE_NAME_FIELD_DESC);
      oprot.writeString(this.packageName);
      oprot.writeFieldEnd();
    }
    oprot.writeFieldBegin(DOWNLOAD_NO_FIELD_DESC);
    oprot.writeI64(this.downloadNo);
    oprot.writeFieldEnd();
    oprot.writeFieldBegin(PRICE_FIELD_DESC);
    oprot.writeI64(this.price);
    oprot.writeFieldEnd();
    oprot.writeFieldBegin(CONSUMPTION_FIELD_DESC);
    oprot.writeI64(this.consumption);
    oprot.writeFieldEnd();
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("MiuiAdStoreServiceLogBiddingEffectRecord(");
    boolean first = true;

    sb.append("scribeInfo:");
    if (this.scribeInfo == null) {
      sb.append("null");
    } else {
      sb.append(this.scribeInfo);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("logType:");
    if (this.logType == null) {
      sb.append("null");
    } else {
      sb.append(this.logType);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("packageName:");
    if (this.packageName == null) {
      sb.append("null");
    } else {
      sb.append(this.packageName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("downloadNo:");
    sb.append(this.downloadNo);
    first = false;
    if (!first) sb.append(", ");
    sb.append("price:");
    sb.append(this.price);
    first = false;
    if (!first) sb.append(", ");
    sb.append("consumption:");
    sb.append(this.consumption);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
  }

}
