<?php

namespace Mobile\Model;

use Think\Model;

class PraiseModel extends Model
{

    protected $tableName = 'praise';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['praise_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['praise_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['praise_id'] = $id;
        return $this->where($where)->getField($filed);
    }

    public function getFiledByKV($key, $value, $filed)
    {
        $where = array();
        $where[$key] = $value;
        return $this->where($where)->getField($filed);
    }

    public function updateByIdWithKV($id, $key, $value)
    {
        $data = array();
        $data[$key] = $value;
        return $this->updateById($id, $data);
    }

    public function insert($uid, $table, $tid, $time)
    {
        $data = array();
        $data['praise_uid'] = $uid;
        $data['praise_table'] = $table;
        $data['praise_tid'] = $tid;
        $data['praise_time'] = $time;
        return $this->add($data);
    }

    //自定义

    public function getNumberByUid($uid)
    {
        $where = array();
        $where['praise_uid'] = $uid;
        $data = $this->where($where)->select();
        return sizeof($data);
    }

    public function deletePraise($table, $tid)
    {
        $where = array();
        $where['praise_table'] = $table;
        $where['praise_tid'] = $tid;
        return $this->where($where)->delete();
    }

    public function isPraise($uid, $table, $tid)
    {
        $where = array();
        $where['praise_uid'] = $uid;
        $where['praise_table'] = $table;
        $where['praise_tid'] = $tid;
        return $this->where($where)->getField('praise_id');
    }

    public function cancelPraise($uid, $table, $tid)
    {
        $where = array();
        $where['praise_uid'] = $uid;
        $where['praise_table'] = $table;
        $where['praise_tid'] = $tid;
        return $this->where($where)->delete();
    }

}