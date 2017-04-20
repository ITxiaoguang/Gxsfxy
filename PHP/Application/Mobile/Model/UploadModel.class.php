<?php

namespace Mobile\Model;

use Think\Model;

class UploadModel extends Model
{

    protected $tableName = 'upload';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['upload_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['upload_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['upload_id'] = $id;
        return $this->where($where)->getField($filed);
    }

    public function updateByIdWithKV($id, $key, $value)
    {
        $data = array();
        $data[$key] = $value;
        return $this->updateById($id, $data);
    }

    public function insert($uid, $class, $type, $tid, $url, $time)
    {
        $data = array();
        $data['upload_uid'] = $uid;
        $data['upload_class'] = $class;
        $data['upload_type'] = $type;
        $data['upload_tid'] = $tid;
        $data['upload_url'] = $url;
        $data['upload_time'] = $time;
        return $this->add($data);
    }

    //自定义

    public function getUrlArray($class, $uid, $type, $tid)
    {
        $where = array();
        $where['upload_class'] = $class;
        $where['upload_uid'] = $uid;
        $where['upload_type'] = $type;
        $where['upload_tid'] = $tid;
        $data = $this->where($where)->field('upload_url')->select();
        $out = array();
        foreach ($data as $key => $val) {
            $out[] = $data[$key]['upload_url'];
        }
        return $out;
    }

}