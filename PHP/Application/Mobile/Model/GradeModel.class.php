<?php

namespace Mobile\Model;

use Think\Model;

class GradeModel extends Model
{

    protected $tableName = 'grade';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['grade_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['grade_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['grade_id'] = $id;
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

    public function insert($uid, $xh, $kkxq, $kcbh, $kcmc, $cj, $xf, $zxs, $khfs, $kcsx, $kcxz)
    {
        $data = array();
        $data['grade_uid'] = $uid;
        $data['grade_xh'] = $xh;
        $data['grade_kkxq'] = $kkxq;
        $data['grade_kcbh'] = $kcbh;
        $data['grade_kcmc'] = $kcmc;
        $data['grade_cj'] = $cj;
        $data['grade_xf'] = $xf;
        $data['grade_zxs'] = $zxs;
        $data['grade_khfs'] = $khfs;
        $data['grade_kcsx'] = $kcsx;
        $data['grade_kcxz'] = $kcxz;
        return $this->add($data);
    }

}