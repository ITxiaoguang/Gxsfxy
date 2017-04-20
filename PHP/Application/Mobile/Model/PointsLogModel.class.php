<?php

namespace Mobile\Model;

use Think\Model;

class PointsLogModel extends Model
{

    protected $tableName = 'points_log';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['pl_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['pl_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['pl_id'] = $id;
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

    public function insert($uid, $type, $points, $content, $time)
    {
        $data = array();
        $data['pl_uid'] = $uid;
        $data['pl_type'] = $type;
        $data['pl_points'] = $points;
        $data['pl_content'] = $content;
        $data['pl_time'] = $time;
        return $this->add($data);
    }

}