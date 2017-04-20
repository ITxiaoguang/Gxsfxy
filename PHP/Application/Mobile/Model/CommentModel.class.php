<?php

namespace Mobile\Model;

use Think\Model;

class CommentModel extends Model
{

    protected $tableName = 'comment';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['comment_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['comment_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['comment_id'] = $id;
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

    public function insert($uid, $table, $tid, $content, $rid, $time)
    {
        $data = array();
        $data['comment_uid'] = $uid;
        $data['comment_table'] = $table;
        $data['comment_tid'] = $tid;
        $data['comment_content'] = $content;
        $data['comment_rid'] = $rid;
        $data['comment_time'] = $time;
        return $this->add($data);
    }

    //自定义

    public function getNumberByUid($uid)
    {
        $where = array();
        $where['comment_uid'] = $uid;
        $data = $this->where($where)->select();
        return sizeof($data);
    }

    public function deleteComment($table, $tid)
    {
        $where = array();
        $where['comment_table'] = $table;
        $where['comment_tid'] = $tid;
        return $this->where($where)->delete();
    }
    
    public function cancelComment($uid, $table, $tid)
    {
        $where = array();
        $where['comment_uid'] = $uid;
        $where['comment_table'] = $table;
        $where['comment_tid'] = $tid;
        return $this->where($where)->delete();
    }

}