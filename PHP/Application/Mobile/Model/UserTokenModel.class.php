<?php

namespace Mobile\Model;

use Think\Model;

class UserTokenModel extends Model
{

    protected $tableName = 'user_token';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['ut_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['ut_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['ut_id'] = $id;
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

    public function insert($uid, $client, $token, $time)
    {
        $data = array();
        $data['ut_uid'] = $uid;
        $data['ut_client'] = $client;
        $data['ut_token'] = $token;
        $data['ut_time'] = $time;
        return $this->add($data);
    }

    //自定义

    public function deleteByUid($id)
    {
        $where = array();
        $where['ut_uid'] = $id;
        return $this->where($where)->delete();
    }

}