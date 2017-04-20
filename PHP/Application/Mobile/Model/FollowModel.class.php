<?php

namespace Mobile\Model;

use Think\Model;

class FollowModel extends Model
{

    protected $tableName = 'follow';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['follow_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['follow_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['follow_id'] = $id;
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

    public function insert($uid, $tid, $time)
    {
        $data = array();
        $data['follow_uid'] = $uid;
        $data['follow_tid'] = $tid;
        $data['follow_time'] = $time;
        return $this->add($data);
    }

    //自定义

    public function isFollow($uid, $tid)
    {
        $where = array();
        $where['follow_uid'] = $uid;
        $where['follow_tid'] = $tid;
        if (empty($this->where($where)->getField('follow_id'))) {
            return false;
        } else {
            return true;
        }
    }

    public function cancelFollow($uid, $tid)
    {
        $where = array();
        $where['follow_uid'] = $uid;
        $where['follow_tid'] = $tid;
        return $this->where($where)->delete();
    }

}