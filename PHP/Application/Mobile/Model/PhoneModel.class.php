<?php

namespace Mobile\Model;

use Think\Model;

class PhoneModel extends Model
{

    protected $tableName = 'phone';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['phone_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['phone_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['phone_id'] = $id;
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

    public function insert($type, $class, $name, $number, $address)
    {
        $data = array();
        $data['phone_type'] = $type;
        $data['phone_class'] = $class;
        $data['phone_name'] = $name;
        $data['phone_number'] = $number;
        $data['phone_address'] = $address;
        return $this->add($data);
    }

}