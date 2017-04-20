<?php

namespace Mobile\Model;

use Think\Model;

class VisitorModel extends Model
{

    protected $tableName = 'visitor';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['visitor_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['visitor_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['visitor_id'] = $id;
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
        $data['visitor_uid'] = $uid;
        $data['visitor_tid'] = $tid;
        $data['visitor_time'] = $time;
        return $this->add($data);
    }

}