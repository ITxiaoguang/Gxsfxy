<?php

namespace Mobile\Model;

use Think\Model;

class AreaModel extends Model
{

    protected $tableName = 'area';

    //公共
    
    public function getRowById($id)
    {
        $where = array();
        $where['area_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['area_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['area_id'] = $id;
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

    public function insert($name, $parent_id)
    {
        $data = array();
        $data['area_name'] = $name;
        $data['area_parent_id'] = $parent_id;
        return $this->add($data);
    }

}