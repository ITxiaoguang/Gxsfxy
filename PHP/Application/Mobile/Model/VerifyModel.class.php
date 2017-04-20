<?php

namespace Mobile\Model;

use Think\Model;

class VerifyModel extends Model
{

    protected $tableName = 'verify';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['verify_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['verify_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['verify_id'] = $id;
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

    public function insert($uid, $type, $code)
    {
        $data = array();
        $data['verify_uid'] = $uid;
        $data['verify_type'] = $type;
        $data['verify_code'] = $code;
        return $this->add($data);
    }

    //自定义

    public function verifyCode($uid, $type, $code)
    {
        $where = array();
        $where['verify_uid'] = $uid;
        $where['verify_type'] = $type;
        $where['verify_code'] = $code;

        $id = $this->where($where)->getField('verify_id');
        if ($id) {
            $where = array();
            $where['verify_id'] = $id;
            $this->where($where)->delete();
            return true;
        } else {
            return false;
        }
    }

}