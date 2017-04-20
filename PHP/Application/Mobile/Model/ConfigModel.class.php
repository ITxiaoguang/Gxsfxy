<?php

namespace Mobile\Model;

use Think\Model;

class ConfigModel extends Model
{

    protected $tableName = 'config';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['config_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['config_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['config_id'] = $id;
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

    public function insert($type, $name, $value)
    {
        $data = array();
        $data['config_type'] = $type;
        $data['config_name'] = $name;
        $data['config_value'] = $value;
        return $this->add($data);
    }

    //自定义
    
    public function updateValue($type, $name, $value)
    {
        $where = array();
        $where['config_type'] = $type;
        $where['config_name'] = $name;
        $data = array();
        $data['config_value'] = $value;
        return $this->where($where)->save($data);
    }

}