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

import com.xiaomi.appstore.thrift.model.AppMarketSearchParam;

public class MiuiAdQueryServiceLogAppStoreSearchExpose implements TBase<MiuiAdQueryServiceLogAppStoreSearchExpose, MiuiAdQueryServiceLogAppStoreSearchExpose._Fields>, java.io.Serializable, Cloneable {
  private static final TStruct STRUCT_DESC = new TStruct("MiuiAdQueryServiceLogAppStoreSearchExpose");

  private static final TField SCRIBE_INFO_FIELD_DESC = new TField("scribeInfo", TType.STRUCT, (short)1);
  private static final TField LOG_TYPE_FIELD_DESC = new TField("logType", TType.STRING, (short)2);
  private static final TField CLIENT_INFO_V3_FIELD_DESC = new TField("clientInfoV3", TType.STRUCT, (short)3);
  private static final TField SEARCH_PARAM_FIELD_DESC = new TField("searchParam", TType.STRUCT, (short)4);
  private static final TField SEARCH_AD_RESULT_FIELD_DESC = new TField("SearchAdResult", TType.STRUCT, (short)5);

  private MiuiLogScribeInfo scribeInfo;
  private String logType;
  private ClientInfoV3 clientInfoV3;
  private AppMarketSearchParam searchParam;
  private SearchAdResult SearchAdResult;

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements TFieldIdEnum {
    SCRIBE_INFO((short)1, "scribeInfo"),
    LOG_TYPE((short)2, "logType"),
    CLIENT_INFO_V3((short)3, "clientInfoV3"),
    SEARCH_PARAM((short)4, "searchParam"),
    SEARCH_AD_RESULT((short)5, "SearchAdResult");

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
        case 3: // CLIENT_INFO_V3
          return CLIENT_INFO_V3;
        case 4: // SEARCH_PARAM
          return SEARCH_PARAM;
        case 5: // SEARCH_AD_RESULT
          return SEARCH_AD_RESULT;
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
    tmpMap.put(_Fields.CLIENT_INFO_V3, new FieldMetaData("clientInfoV3", TFieldRequirementType.DEFAULT, 
        new StructMetaData(TType.STRUCT, ClientInfoV3.class)));
    tmpMap.put(_Fields.SEARCH_PARAM, new FieldMetaData("searchParam", TFieldRequirementType.DEFAULT, 
        new StructMetaData(TType.STRUCT, AppMarketSearchParam.class)));
    tmpMap.put(_Fields.SEARCH_AD_RESULT, new FieldMetaData("SearchAdResult", TFieldRequirementType.DEFAULT, 
        new StructMetaData(TType.STRUCT, SearchAdResult.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    FieldMetaData.addStructMetaDataMap(MiuiAdQueryServiceLogAppStoreSearchExpose.class, metaDataMap);
  }

  public MiuiAdQueryServiceLogAppStoreSearchExpose() {
  }

  public MiuiAdQueryServiceLogAppStoreSearchExpose(
    MiuiLogScribeInfo scribeInfo,
    String logType,
    ClientInfoV3 clientInfoV3,
    AppMarketSearchParam searchParam,
    SearchAdResult SearchAdResult)
  {
    this();
    this.scribeInfo = scribeInfo;
    this.logType = logType;
    this.clientInfoV3 = clientInfoV3;
    this.searchParam = searchParam;
    this.SearchAdResult = SearchAdResult;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MiuiAdQueryServiceLogAppStoreSearchExpose(MiuiAdQueryServiceLogAppStoreSearchExpose other) {
    if (other.isSetScribeInfo()) {
      this.scribeInfo = new MiuiLogScribeInfo(other.scribeInfo);
    }
    if (other.isSetLogType()) {
      this.logType = other.logType;
    }
    if (other.isSetClientInfoV3()) {
      this.clientInfoV3 = new ClientInfoV3(other.clientInfoV3);
    }
    if (other.isSetSearchParam()) {
      this.searchParam = new AppMarketSearchParam(other.searchParam);
    }
    if (other.isSetSearchAdResult()) {
      this.SearchAdResult = new SearchAdResult(other.SearchAdResult);
    }
  }

  public MiuiAdQueryServiceLogAppStoreSearchExpose deepCopy() {
    return new MiuiAdQueryServiceLogAppStoreSearchExpose(this);
  }

  @Override
  public void clear() {
    this.scribeInfo = null;
    this.logType = null;
    this.clientInfoV3 = null;
    this.searchParam = null;
    this.SearchAdResult = null;
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

  public ClientInfoV3 getClientInfoV3() {
    return this.clientInfoV3;
  }

  public void setClientInfoV3(ClientInfoV3 clientInfoV3) {
    this.clientInfoV3 = clientInfoV3;
  }

  public void unsetClientInfoV3() {
    this.clientInfoV3 = null;
  }

  /** Returns true if field clientInfoV3 is set (has been asigned a value) and false otherwise */
  public boolean isSetClientInfoV3() {
    return this.clientInfoV3 != null;
  }

  public void setClientInfoV3IsSet(boolean value) {
    if (!value) {
      this.clientInfoV3 = null;
    }
  }

  public AppMarketSearchParam getSearchParam() {
    return this.searchParam;
  }

  public void setSearchParam(AppMarketSearchParam searchParam) {
    this.searchParam = searchParam;
  }

  public void unsetSearchParam() {
    this.searchParam = null;
  }

  /** Returns true if field searchParam is set (has been asigned a value) and false otherwise */
  public boolean isSetSearchParam() {
    return this.searchParam != null;
  }

  public void setSearchParamIsSet(boolean value) {
    if (!value) {
      this.searchParam = null;
    }
  }

  public SearchAdResult getSearchAdResult() {
    return this.SearchAdResult;
  }

  public void setSearchAdResult(SearchAdResult SearchAdResult) {
    this.SearchAdResult = SearchAdResult;
  }

  public void unsetSearchAdResult() {
    this.SearchAdResult = null;
  }

  /** Returns true if field SearchAdResult is set (has been asigned a value) and false otherwise */
  public boolean isSetSearchAdResult() {
    return this.SearchAdResult != null;
  }

  public void setSearchAdResultIsSet(boolean value) {
    if (!value) {
      this.SearchAdResult = null;
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

    case CLIENT_INFO_V3:
      if (value == null) {
        unsetClientInfoV3();
      } else {
        setClientInfoV3((ClientInfoV3)value);
      }
      break;

    case SEARCH_PARAM:
      if (value == null) {
        unsetSearchParam();
      } else {
        setSearchParam((AppMarketSearchParam)value);
      }
      break;

    case SEARCH_AD_RESULT:
      if (value == null) {
        unsetSearchAdResult();
      } else {
        setSearchAdResult((SearchAdResult)value);
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

    case CLIENT_INFO_V3:
      return getClientInfoV3();

    case SEARCH_PARAM:
      return getSearchParam();

    case SEARCH_AD_RESULT:
      return getSearchAdResult();

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
    case CLIENT_INFO_V3:
      return isSetClientInfoV3();
    case SEARCH_PARAM:
      return isSetSearchParam();
    case SEARCH_AD_RESULT:
      return isSetSearchAdResult();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof MiuiAdQueryServiceLogAppStoreSearchExpose)
      return this.equals((MiuiAdQueryServiceLogAppStoreSearchExpose)that);
    return false;
  }

  public boolean equals(MiuiAdQueryServiceLogAppStoreSearchExpose that) {
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

    boolean this_present_clientInfoV3 = true && this.isSetClientInfoV3();
    boolean that_present_clientInfoV3 = true && that.isSetClientInfoV3();
    if (this_present_clientInfoV3 || that_present_clientInfoV3) {
      if (!(this_present_clientInfoV3 && that_present_clientInfoV3))
        return false;
      if (!this.clientInfoV3.equals(that.clientInfoV3))
        return false;
    }

    boolean this_present_searchParam = true && this.isSetSearchParam();
    boolean that_present_searchParam = true && that.isSetSearchParam();
    if (this_present_searchParam || that_present_searchParam) {
      if (!(this_present_searchParam && that_present_searchParam))
        return false;
      if (!this.searchParam.equals(that.searchParam))
        return false;
    }

    boolean this_present_SearchAdResult = true && this.isSetSearchAdResult();
    boolean that_present_SearchAdResult = true && that.isSetSearchAdResult();
    if (this_present_SearchAdResult || that_present_SearchAdResult) {
      if (!(this_present_SearchAdResult && that_present_SearchAdResult))
        return false;
      if (!this.SearchAdResult.equals(that.SearchAdResult))
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

    boolean present_clientInfoV3 = true && (isSetClientInfoV3());
    builder.append(present_clientInfoV3);
    if (present_clientInfoV3)
      builder.append(clientInfoV3);

    boolean present_searchParam = true && (isSetSearchParam());
    builder.append(present_searchParam);
    if (present_searchParam)
      builder.append(searchParam);

    boolean present_SearchAdResult = true && (isSetSearchAdResult());
    builder.append(present_SearchAdResult);
    if (present_SearchAdResult)
      builder.append(SearchAdResult);

    return builder.toHashCode();
  }

  public int compareTo(MiuiAdQueryServiceLogAppStoreSearchExpose other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    MiuiAdQueryServiceLogAppStoreSearchExpose typedOther = (MiuiAdQueryServiceLogAppStoreSearchExpose)other;

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
    lastComparison = Boolean.valueOf(isSetClientInfoV3()).compareTo(typedOther.isSetClientInfoV3());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetClientInfoV3()) {
      lastComparison = TBaseHelper.compareTo(this.clientInfoV3, typedOther.clientInfoV3);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetSearchParam()).compareTo(typedOther.isSetSearchParam());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSearchParam()) {
      lastComparison = TBaseHelper.compareTo(this.searchParam, typedOther.searchParam);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetSearchAdResult()).compareTo(typedOther.isSetSearchAdResult());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSearchAdResult()) {
      lastComparison = TBaseHelper.compareTo(this.SearchAdResult, typedOther.SearchAdResult);
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
        case 3: // CLIENT_INFO_V3
          if (field.type == TType.STRUCT) {
            this.clientInfoV3 = new ClientInfoV3();
            this.clientInfoV3.read(iprot);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 4: // SEARCH_PARAM
          if (field.type == TType.STRUCT) {
            this.searchParam = new AppMarketSearchParam();
            this.searchParam.read(iprot);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 5: // SEARCH_AD_RESULT
          if (field.type == TType.STRUCT) {
            this.SearchAdResult = new SearchAdResult();
            this.SearchAdResult.read(iprot);
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
    if (this.clientInfoV3 != null) {
      oprot.writeFieldBegin(CLIENT_INFO_V3_FIELD_DESC);
      this.clientInfoV3.write(oprot);
      oprot.writeFieldEnd();
    }
    if (this.searchParam != null) {
      oprot.writeFieldBegin(SEARCH_PARAM_FIELD_DESC);
      this.searchParam.write(oprot);
      oprot.writeFieldEnd();
    }
    if (this.SearchAdResult != null) {
      oprot.writeFieldBegin(SEARCH_AD_RESULT_FIELD_DESC);
      this.SearchAdResult.write(oprot);
      oprot.writeFieldEnd();
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("MiuiAdQueryServiceLogAppStoreSearchExpose(");
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
    sb.append("clientInfoV3:");
    if (this.clientInfoV3 == null) {
      sb.append("null");
    } else {
      sb.append(this.clientInfoV3);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("searchParam:");
    if (this.searchParam == null) {
      sb.append("null");
    } else {
      sb.append(this.searchParam);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("SearchAdResult:");
    if (this.SearchAdResult == null) {
      sb.append("null");
    } else {
      sb.append(this.SearchAdResult);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
  }

}
