<?php

namespace Mobile\Model;

use Think\Model;

class LogModel extends Model
{

    protected $tableName = 'log';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['log_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['log_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['log_id'] = $id;
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

    public function insert($model, $control, $action, $state, $content, $client, $version, $device, $time)
    {
        $data = array();
        $data['log_model'] = $model;
        $data['log_control'] = $control;
        $data['log_action'] = $action;
        $data['log_state'] = $state;
        $data['log_content'] = $content;
        $data['log_client'] = $client;
        $data['log_version'] = $version;
        $data['log_device'] = $device;
        $data['log_time'] = $time;
        return $this->add($data);
    }

}