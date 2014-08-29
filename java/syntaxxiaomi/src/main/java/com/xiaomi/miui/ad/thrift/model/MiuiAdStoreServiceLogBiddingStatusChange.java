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

public class MiuiAdStoreServiceLogBiddingStatusChange implements TBase<MiuiAdStoreServiceLogBiddingStatusChange, MiuiAdStoreServiceLogBiddingStatusChange._Fields>, java.io.Serializable, Cloneable {
  private static final TStruct STRUCT_DESC = new TStruct("MiuiAdStoreServiceLogBiddingStatusChange");

  private static final TField SCRIBE_INFO_FIELD_DESC = new TField("scribeInfo", TType.STRUCT, (short)1);
  private static final TField LOG_TYPE_FIELD_DESC = new TField("logType", TType.STRING, (short)2);
  private static final TField PACKAGE_NAME_FIELD_DESC = new TField("packageName", TType.STRING, (short)3);
  private static final TField FROM_STATUS_FIELD_DESC = new TField("fromStatus", TType.STRING, (short)4);
  private static final TField TO_STATUS_FIELD_DESC = new TField("toStatus", TType.STRING, (short)5);

  private MiuiLogScribeInfo scribeInfo;
  private String logType;
  private String packageName;
  private String fromStatus;
  private String toStatus;

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements TFieldIdEnum {
    SCRIBE_INFO((short)1, "scribeInfo"),
    LOG_TYPE((short)2, "logType"),
    PACKAGE_NAME((short)3, "packageName"),
    FROM_STATUS((short)4, "fromStatus"),
    TO_STATUS((short)5, "toStatus");

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
        case 4: // FROM_STATUS
          return FROM_STATUS;
        case 5: // TO_STATUS
          return TO_STATUS;
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

  public static final Map<_Fields, FieldMetaData> metaDataMap;
  static {
    Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SCRIBE_INFO, new FieldMetaData("scribeInfo", TFieldRequirementType.DEFAULT, 
        new StructMetaData(TType.STRUCT, MiuiLogScribeInfo.class)));
    tmpMap.put(_Fields.LOG_TYPE, new FieldMetaData("logType", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.STRING)));
    tmpMap.put(_Fields.PACKAGE_NAME, new FieldMetaData("packageName", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.STRING)));
    tmpMap.put(_Fields.FROM_STATUS, new FieldMetaData("fromStatus", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.STRING)));
    tmpMap.put(_Fields.TO_STATUS, new FieldMetaData("toStatus", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    FieldMetaData.addStructMetaDataMap(MiuiAdStoreServiceLogBiddingStatusChange.class, metaDataMap);
  }

  public MiuiAdStoreServiceLogBiddingStatusChange() {
  }

  public MiuiAdStoreServiceLogBiddingStatusChange(
    MiuiLogScribeInfo scribeInfo,
    String logType,
    String packageName,
    String fromStatus,
    String toStatus)
  {
    this();
    this.scribeInfo = scribeInfo;
    this.logType = logType;
    this.packageName = packageName;
    this.fromStatus = fromStatus;
    this.toStatus = toStatus;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MiuiAdStoreServiceLogBiddingStatusChange(MiuiAdStoreServiceLogBiddingStatusChange other) {
    if (other.isSetScribeInfo()) {
      this.scribeInfo = new MiuiLogScribeInfo(other.scribeInfo);
    }
    if (other.isSetLogType()) {
      this.logType = other.logType;
    }
    if (other.isSetPackageName()) {
      this.packageName = other.packageName;
    }
    if (other.isSetFromStatus()) {
      this.fromStatus = other.fromStatus;
    }
    if (other.isSetToStatus()) {
      this.toStatus = other.toStatus;
    }
  }

  public MiuiAdStoreServiceLogBiddingStatusChange deepCopy() {
    return new MiuiAdStoreServiceLogBiddingStatusChange(this);
  }

  @Override
  public void clear() {
    this.scribeInfo = null;
    this.logType = null;
    this.packageName = null;
    this.fromStatus = null;
    this.toStatus = null;
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

  public String getFromStatus() {
    return this.fromStatus;
  }

  public void setFromStatus(String fromStatus) {
    this.fromStatus = fromStatus;
  }

  public void unsetFromStatus() {
    this.fromStatus = null;
  }

  /** Returns true if field fromStatus is set (has been asigned a value) and false otherwise */
  public boolean isSetFromStatus() {
    return this.fromStatus != null;
  }

  public void setFromStatusIsSet(boolean value) {
    if (!value) {
      this.fromStatus = null;
    }
  }

  public String getToStatus() {
    return this.toStatus;
  }

  public void setToStatus(String toStatus) {
    this.toStatus = toStatus;
  }

  public void unsetToStatus() {
    this.toStatus = null;
  }

  /** Returns true if field toStatus is set (has been asigned a value) and false otherwise */
  public boolean isSetToStatus() {
    return this.toStatus != null;
  }

  public void setToStatusIsSet(boolean value) {
    if (!value) {
      this.toStatus = null;
    }
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

    case FROM_STATUS:
      if (value == null) {
        unsetFromStatus();
      } else {
        setFromStatus((String)value);
      }
      break;

    case TO_STATUS:
      if (value == null) {
        unsetToStatus();
      } else {
        setToStatus((String)value);
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

    case FROM_STATUS:
      return getFromStatus();

    case TO_STATUS:
      return getToStatus();

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
    case FROM_STATUS:
      return isSetFromStatus();
    case TO_STATUS:
      return isSetToStatus();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof MiuiAdStoreServiceLogBiddingStatusChange)
      return this.equals((MiuiAdStoreServiceLogBiddingStatusChange)that);
    return false;
  }

  public boolean equals(MiuiAdStoreServiceLogBiddingStatusChange that) {
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

    boolean this_present_fromStatus = true && this.isSetFromStatus();
    boolean that_present_fromStatus = true && that.isSetFromStatus();
    if (this_present_fromStatus || that_present_fromStatus) {
      if (!(this_present_fromStatus && that_present_fromStatus))
        return false;
      if (!this.fromStatus.equals(that.fromStatus))
        return false;
    }

    boolean this_present_toStatus = true && this.isSetToStatus();
    boolean that_present_toStatus = true && that.isSetToStatus();
    if (this_present_toStatus || that_present_toStatus) {
      if (!(this_present_toStatus && that_present_toStatus))
        return false;
      if (!this.toStatus.equals(that.toStatus))
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

    boolean present_fromStatus = true && (isSetFromStatus());
    builder.append(present_fromStatus);
    if (present_fromStatus)
      builder.append(fromStatus);

    boolean present_toStatus = true && (isSetToStatus());
    builder.append(present_toStatus);
    if (present_toStatus)
      builder.append(toStatus);

    return builder.toHashCode();
  }

  public int compareTo(MiuiAdStoreServiceLogBiddingStatusChange other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    MiuiAdStoreServiceLogBiddingStatusChange typedOther = (MiuiAdStoreServiceLogBiddingStatusChange)other;

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
    lastComparison = Boolean.valueOf(isSetFromStatus()).compareTo(typedOther.isSetFromStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFromStatus()) {
      lastComparison = TBaseHelper.compareTo(this.fromStatus, typedOther.fromStatus);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetToStatus()).compareTo(typedOther.isSetToStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetToStatus()) {
      lastComparison = TBaseHelper.compareTo(this.toStatus, typedOther.toStatus);
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
        case 4: // FROM_STATUS
          if (field.type == TType.STRING) {
            this.fromStatus = iprot.readString();
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 5: // TO_STATUS
          if (field.type == TType.STRING) {
            this.toStatus = iprot.readString();
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
    if (this.fromStatus != null) {
      oprot.writeFieldBegin(FROM_STATUS_FIELD_DESC);
      oprot.writeString(this.fromStatus);
      oprot.writeFieldEnd();
    }
    if (this.toStatus != null) {
      oprot.writeFieldBegin(TO_STATUS_FIELD_DESC);
      oprot.writeString(this.toStatus);
      oprot.writeFieldEnd();
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("MiuiAdStoreServiceLogBiddingStatusChange(");
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
    sb.append("fromStatus:");
    if (this.fromStatus == null) {
      sb.append("null");
    } else {
      sb.append(this.fromStatus);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("toStatus:");
    if (this.toStatus == null) {
      sb.append("null");
    } else {
      sb.append(this.toStatus);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
  }

}

