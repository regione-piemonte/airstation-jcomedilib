#include <math.h>
#include <comedilib.h>
#include "it_csi_aria_jcomedilib_Comedi.h"

#ifndef NAN
#define NAN \
  (__extension__ ((union { unsigned char __c[8];                              \
                           double __d; })                                     \
                  { { 0, 0, 0, 0, 0, 0, 0xf8, 0x7f } }).__d)
#endif

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_close 
  (JNIEnv *env, jclass obj, jlong deviceDescriptor)
{
  return(comedi_close((comedi_t *)deviceDescriptor));
}

JNIEXPORT jlong JNICALL Java_it_csi_aria_jcomedilib_Comedi_open
  (JNIEnv *env, jclass obj, jstring filename)
{
  const char *str_filename = (*env)->GetStringUTFChars(env, filename, 0);
  comedi_t *devDesc = comedi_open(str_filename);
  (*env)->ReleaseStringUTFChars(env, filename, str_filename);
  return((jlong)devDesc);
}

JNIEXPORT jstring JNICALL Java_it_csi_aria_jcomedilib_Comedi_strerror
  (JNIEnv *env, jclass obj, jint errnum)
{
  const char *str_error = comedi_strerror(errnum);
  return((*env)->NewStringUTF(env, str_error));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_errno
  (JNIEnv *env, jclass obj)
{
  return(comedi_errno());
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1n_1subdevices
  (JNIEnv *env, jclass obj, jlong deviceDescriptor)
{
  return(comedi_get_n_subdevices((comedi_t *)deviceDescriptor));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1version_1code
  (JNIEnv *env, jclass obj, jlong deviceDescriptor)
{
  return(comedi_get_version_code((comedi_t *)deviceDescriptor));
}

JNIEXPORT jstring JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1driver_1name
  (JNIEnv *env, jclass obj, jlong deviceDescriptor)
{
  const char *str_name = comedi_get_driver_name((comedi_t *)deviceDescriptor);
  return((*env)->NewStringUTF(env, str_name));
}

JNIEXPORT jstring JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1board_1name
  (JNIEnv *env, jclass obj, jlong deviceDescriptor)
{
  const char *str_name = comedi_get_board_name((comedi_t *)deviceDescriptor);
  return((*env)->NewStringUTF(env, str_name));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1subdevice_1type
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice)
{
  return(comedi_get_subdevice_type((comedi_t *)deviceDescriptor, subdevice));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_find_1subdevice_1by_1type
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint type, jint start_subdevice)
{
  return(comedi_find_subdevice_by_type((comedi_t *)deviceDescriptor,
                                       type, start_subdevice));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1subdevice_1flags
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice)
{
  return(comedi_get_subdevice_flags((comedi_t *)deviceDescriptor, subdevice));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1n_1channels
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice)
{
  return(comedi_get_n_channels((comedi_t *)deviceDescriptor, subdevice));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_range_1is_1chan_1specific
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice)
{
  return(comedi_range_is_chan_specific((comedi_t *)deviceDescriptor, subdevice));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_maxdata_1is_1chan_1specific
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice)
{
  return(comedi_maxdata_is_chan_specific((comedi_t *)deviceDescriptor, subdevice));
}

JNIEXPORT jobject JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1maxdata
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice, jint channel)
{
  jint value;
  jclass clsLSamplT;
  jmethodID midLSamplT;
  jobject objLSamplT;
  
  value = comedi_get_maxdata((comedi_t *)deviceDescriptor, subdevice, channel);
  clsLSamplT = (*env)->FindClass(env, "it/csi/aria/jcomedilib/LSamplT");
  if (clsLSamplT == NULL)
    return(NULL);
  midLSamplT = (*env)->GetMethodID(env, clsLSamplT, "<init>", "(I)V");
  if (midLSamplT == NULL)
    return(NULL);
  objLSamplT = (*env)->NewObject(env, clsLSamplT, midLSamplT, value);
  return(objLSamplT);
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1n_1ranges
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice, jint channel)
{
  return(comedi_get_n_ranges((comedi_t *)deviceDescriptor, subdevice, channel));
}

JNIEXPORT jobject JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1range
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice, jint channel,
   jint range)
{
  comedi_range *c_range;
  jclass clsComediRange;
  jmethodID midComediRange;
  jobject objComediRange;
  c_range = comedi_get_range((comedi_t *)deviceDescriptor,
                             subdevice, channel, range);
  if (c_range == NULL)
    return(NULL);
  clsComediRange = (*env)->FindClass(env, "it/csi/aria/jcomedilib/ComediRange");
  if (clsComediRange == NULL)
    return(NULL);
  midComediRange = (*env)->GetMethodID(env, clsComediRange, "<init>", "(DDI)V");
  if (midComediRange == NULL)
    return(NULL);
  objComediRange = (*env)->NewObject(env, clsComediRange, midComediRange,
                                     c_range->min, c_range->max, c_range->unit);
  return(objComediRange);
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_find_1range
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice, jint channel,
   jint unit, jdouble min, jdouble max)
{
  return(comedi_find_range((comedi_t *)deviceDescriptor, subdevice, channel,
                           unit, min, max));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1buffer_1size
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice)
{
  return(comedi_get_buffer_size((comedi_t *)deviceDescriptor, subdevice));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_get_1max_1buffer_1size
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice)
{
  return(comedi_get_max_buffer_size((comedi_t *)deviceDescriptor, subdevice));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_set_1buffer_1size
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice, jint size)
{
  return(comedi_set_buffer_size((comedi_t *)deviceDescriptor, subdevice, size));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_do_1insnlist
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jobjectArray list)
{
  /* TBD: Completare !!!! */
  jclass excCls =
    (*env)->FindClass(env, "java/lang/UnsupportedOperationException");
  if (excCls != NULL)
    (*env)->ThrowNew(env, excCls, "Not yet implemented");
  return(-1);
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_do_1insn
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jobject instruction)
{
  jclass cls_ins;
  jfieldID fid_insn, fid_data, fid_subdev, fid_chanspec, fid_unused;
  comedi_insn c_insn;
  jintArray array_data;
  jint *body_data;
  jsize length_data;
  jint result;

  if (instruction == NULL)
    goto jni_error;
  cls_ins = (*env)->GetObjectClass(env, instruction);
  if (cls_ins == NULL)
    goto jni_error;
  fid_insn = (*env)->GetFieldID(env, cls_ins, "insn", "I");
  fid_data = (*env)->GetFieldID(env, cls_ins, "data", "[I");
  fid_subdev = (*env)->GetFieldID(env, cls_ins, "subdev", "I");
  fid_chanspec = (*env)->GetFieldID(env, cls_ins, "chanspec", "I");
  fid_unused = (*env)->GetFieldID(env, cls_ins, "unused", "[I");
  if (fid_insn == NULL || fid_data == NULL || fid_subdev == NULL ||
      fid_chanspec == NULL || fid_unused == NULL)
    goto jni_error;

  switch ((*env)->GetIntField(env, instruction, fid_insn))
    {
    case it_csi_aria_jcomedilib_Comedi_INSN_READ:
      c_insn.insn = INSN_READ; break;
    case it_csi_aria_jcomedilib_Comedi_INSN_WRITE:
      c_insn.insn = INSN_WRITE; break;
    case it_csi_aria_jcomedilib_Comedi_INSN_BITS:
      c_insn.insn = INSN_BITS; break;
    case it_csi_aria_jcomedilib_Comedi_INSN_CONFIG:
      c_insn.insn = INSN_CONFIG; break;
    case it_csi_aria_jcomedilib_Comedi_INSN_GTOD:
      c_insn.insn = INSN_GTOD; break;
    case it_csi_aria_jcomedilib_Comedi_INSN_WAIT:
      c_insn.insn = INSN_WAIT; break;
    case it_csi_aria_jcomedilib_Comedi_INSN_INTTRIG:
      c_insn.insn = INSN_INTTRIG; break;
    default:
      goto jni_error;
    }
  array_data = (*env)->GetObjectField(env, instruction, fid_data);
  if (array_data == NULL)
    goto jni_error;
  body_data = (*env)->GetIntArrayElements(env, array_data, NULL);
  if (body_data == NULL)
    goto jni_error;
  length_data = (*env)->GetArrayLength(env, array_data);
  c_insn.n = length_data;
  c_insn.data = (lsampl_t *)body_data;
  c_insn.subdev = (*env)->GetIntField(env, instruction, fid_subdev);
  c_insn.chanspec = (*env)->GetIntField(env, instruction, fid_chanspec);

  result = comedi_do_insn((comedi_t *)deviceDescriptor, &c_insn);

  (*env)->ReleaseIntArrayElements(env, array_data, body_data, 0);
  
  return(result);

 jni_error: {
    jclass excCls =
      (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    if (excCls != NULL)
      (*env)->ThrowNew(env, excCls, "");
    return(-1);
  }
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_lock
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice)
{
  return(comedi_lock((comedi_t *)deviceDescriptor, subdevice));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_unlock
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice)
{
  return(comedi_unlock((comedi_t *)deviceDescriptor, subdevice));
}

JNIEXPORT jdouble JNICALL Java_it_csi_aria_jcomedilib_Comedi_to_1phys
  (JNIEnv *env, jclass obj, jobject data, jobject range, jobject maxdata)
{
  jclass cls_data;
  jclass cls_range;
  jfieldID fid_data_value;
  jfieldID fid_range_min, fid_range_max, fid_range_unit;
  lsampl_t lst_data;
  comedi_range c_range;
  jclass cls_maxdata;
  jfieldID fid_maxdata_value;
  lsampl_t lst_maxdata;

  if (data == NULL || range == NULL || maxdata == NULL)
    goto jni_error;
  
  cls_data = (*env)->GetObjectClass(env, data);
  if (cls_data == NULL)
    goto jni_error;
  fid_data_value = (*env)->GetFieldID(env, cls_data, "value", "I");
  if (fid_data_value == NULL)
    goto jni_error;
  lst_data = (*env)->GetIntField(env, data, fid_data_value);

  cls_range = (*env)->GetObjectClass(env, range);
  if (cls_range == NULL)
    goto jni_error;
  fid_range_min = (*env)->GetFieldID(env, cls_range, "min", "D");
  fid_range_max = (*env)->GetFieldID(env, cls_range, "max", "D");
  fid_range_unit = (*env)->GetFieldID(env, cls_range, "unit", "I");
  if (fid_range_min == NULL || fid_range_max == NULL ||
      fid_range_unit == NULL)
    goto jni_error;
  c_range.min = (*env)->GetDoubleField(env, range, fid_range_min);
  c_range.max = (*env)->GetDoubleField(env, range, fid_range_max);
  c_range.unit = (*env)->GetIntField(env, range, fid_range_unit);

  cls_maxdata = (*env)->GetObjectClass(env, maxdata);
  if (cls_maxdata == NULL)
    goto jni_error;
  fid_maxdata_value = (*env)->GetFieldID(env, cls_maxdata, "value", "I");
  if (fid_maxdata_value == NULL)
    goto jni_error;
  lst_maxdata = (*env)->GetIntField(env, maxdata, fid_maxdata_value);
  
  return(comedi_to_phys(lst_data, &c_range, lst_maxdata));
  
 jni_error: {
    jclass excCls =
      (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    if (excCls != NULL)
      (*env)->ThrowNew(env, excCls, "");
    return(NAN);
  }
}

JNIEXPORT jobject JNICALL Java_it_csi_aria_jcomedilib_Comedi_from_1phys
  (JNIEnv *env, jclass obj, jdouble data, jobject range, jobject maxdata)
{
  jclass cls_range;
  jfieldID fid_range_min, fid_range_max, fid_range_unit;
  comedi_range c_range;
  jclass cls_maxdata;
  jfieldID fid_maxdata_value;
  lsampl_t lst_maxdata, value;
  jclass clsLSamplT;
  jmethodID midLSamplT;

  if (range == NULL || maxdata == NULL)
    goto jni_error;
  
  cls_range = (*env)->GetObjectClass(env, range);
  if (cls_range == NULL)
    goto jni_error;
  fid_range_min = (*env)->GetFieldID(env, cls_range, "min", "D");
  fid_range_max = (*env)->GetFieldID(env, cls_range, "max", "D");
  fid_range_unit = (*env)->GetFieldID(env, cls_range, "unit", "I");
  if (fid_range_min == NULL || fid_range_max == NULL ||
      fid_range_unit == NULL)
    goto jni_error;
  c_range.min = (*env)->GetDoubleField(env, range, fid_range_min);
  c_range.max = (*env)->GetDoubleField(env, range, fid_range_max);
  c_range.unit = (*env)->GetIntField(env, range, fid_range_unit);

  cls_maxdata = (*env)->GetObjectClass(env, maxdata);
  if (cls_maxdata == NULL)
    goto jni_error;
  fid_maxdata_value = (*env)->GetFieldID(env, cls_maxdata, "value", "I");
  if (fid_maxdata_value == NULL)
    goto jni_error;
  lst_maxdata = (*env)->GetIntField(env, maxdata, fid_maxdata_value);

  value = comedi_from_phys(data, &c_range, lst_maxdata);

  clsLSamplT = (*env)->FindClass(env, "it/csi/aria/jcomedilib/LSamplT");
  if (clsLSamplT == NULL)
    return(NULL);
  midLSamplT = (*env)->GetMethodID(env, clsLSamplT, "<init>","(I)V");
  if (midLSamplT == NULL)
    return(NULL);
  return((*env)->NewObject(env, clsLSamplT, midLSamplT, value));
 
 jni_error: {
    jclass excCls =
      (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    if (excCls != NULL)
      (*env)->ThrowNew(env, excCls, "");
    return(NULL);
  }
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_data_1read
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice,
   jint channel, jint range, jint aref, jobject data)
{
  jclass cls_data;
  jfieldID fid_data_value;
  lsampl_t lst_data = 0;
  jint result;

  if (data == NULL)
    goto jni_error;
  
  cls_data = (*env)->GetObjectClass(env, data);
  if (cls_data == NULL)
    goto jni_error;
  fid_data_value = (*env)->GetFieldID(env, cls_data, "value", "I");
  if (fid_data_value == NULL)
    goto jni_error;

  result = comedi_data_read((comedi_t *)deviceDescriptor, subdevice,
                            channel, range, aref, &lst_data);

  (*env)->SetIntField(env, data, fid_data_value, lst_data);

  return(result);
  
 jni_error: {
    jclass excCls =
      (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    if (excCls != NULL)
      (*env)->ThrowNew(env, excCls, "");
    return(-1);
  }
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_data_1read_1delayed
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice,
   jint channel, jint range, jint aref, jobject data, jint nanosec)
{
  jclass cls_data;
  jfieldID fid_data_value;
  lsampl_t lst_data = 0;
  jint result;

  if (data == NULL)
    goto jni_error;
  
  cls_data = (*env)->GetObjectClass(env, data);
  if (cls_data == NULL)
    goto jni_error;
  fid_data_value = (*env)->GetFieldID(env, cls_data, "value", "I");
  if (fid_data_value == NULL)
    goto jni_error;

  result = comedi_data_read_delayed((comedi_t *)deviceDescriptor, subdevice,
                            channel, range, aref, &lst_data, nanosec);

  (*env)->SetIntField(env, data, fid_data_value, lst_data);

  return(result);
  
 jni_error: {
    jclass excCls =
      (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    if (excCls != NULL)
      (*env)->ThrowNew(env, excCls, "");
    return(-1);
  }
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_data_1read_1hint
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice,
   jint channel, jint range, jint aref)
{
  return(comedi_data_read_hint((comedi_t *)deviceDescriptor, subdevice,
                               channel, range, aref));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_data_1write
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice,
   jint channel, jint range, jint aref, jobject data)
{
  jclass cls_data;
  jfieldID fid_data_value;
  lsampl_t lst_data;

  if (data == NULL)
    goto jni_error;
  
  cls_data = (*env)->GetObjectClass(env, data);
  if (cls_data == NULL)
    goto jni_error;
  fid_data_value = (*env)->GetFieldID(env, cls_data, "value", "I");
  if (fid_data_value == NULL)
    goto jni_error;
  lst_data = (*env)->GetIntField(env, data, fid_data_value);
  
  return(comedi_data_write((comedi_t *)deviceDescriptor, subdevice,
                           channel, range, aref, lst_data));
  
 jni_error: {
    jclass excCls =
      (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    if (excCls != NULL)
      (*env)->ThrowNew(env, excCls, "");
    return(-1);
  }
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_dio_1config
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice,
   jint channel, jint direction)
{
  return(comedi_dio_config((comedi_t *)deviceDescriptor, subdevice,
                           channel, direction));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_dio_1read
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice,
   jint channel, jintArray bit)
{
  jint result = -1;
  jsize len = (*env)->GetArrayLength(env, bit);
  jint *body = (*env)->GetIntArrayElements(env, bit, 0);
  if (len > 0) {
    result = comedi_dio_read((comedi_t *)deviceDescriptor, subdevice,
                             channel, (unsigned int *)body);
  }
  (*env)->ReleaseIntArrayElements(env, bit, body, 0);

  return(result);
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_dio_1write
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice,
   jint channel, jint bit)
{
  return(comedi_dio_write((comedi_t *)deviceDescriptor, subdevice,
                          channel, bit));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_dio_1bitfield
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice,
   jint write_mask, jintArray bits)
{
  jint result = -1;
  jsize len = (*env)->GetArrayLength(env, bits);
  jint *body = (*env)->GetIntArrayElements(env, bits, 0);
  if (len > 0) {
    result = comedi_dio_bitfield((comedi_t *)deviceDescriptor, subdevice,
                                 write_mask, (unsigned int *)body);
  }
  (*env)->ReleaseIntArrayElements(env, bits, body, 0);

  return(result);
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_set_1max_1buffer_1size
  (JNIEnv *env, jclass obj, jlong deviceDescriptor, jint subdevice, jint max_size)
{
  return(comedi_set_max_buffer_size((comedi_t *)deviceDescriptor,
                                    subdevice, max_size));
}

JNIEXPORT jint JNICALL Java_it_csi_aria_jcomedilib_Comedi_set_1global_1oor_1behavior
  (JNIEnv *env, jclass obj, jint behavior)
{
  return(comedi_set_global_oor_behavior(behavior));
}

