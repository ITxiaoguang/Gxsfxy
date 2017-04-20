<?php

namespace Mobile\Model;

use Think\Model;

class ActivityModel extends Model
{

    protected $tableName = 'activity';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['activity_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['activity_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['activity_id'] = $id;
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

    public function insert($name, $image, $content, $start_time, $end_time)
    {
        $data = array();
        $data['activity_name'] = $name;
        $data['activity_image'] = $image;
        $data['activity_praise'] = '0';
        $data['activity_comment'] = '0';
        $data['activity_content'] = $content;
        $data['activity_start_time'] = $start_time;
        $data['activity_end_time'] = $end_time;
        $data['activity_state'] = '0';
        return $this->add($data);
    }

    //自定义

    public function addPraiseNumber($id)
    {
        $praise = $this->getFiledById($id, 'news_praise');
        $praise = intval($praise) + 1;
        return $this->updateByIdWithKV($id, 'news_praise', $praise);
    }

    public function subPraiseNumber($id)
    {
        $praise = $this->getFiledById($id, 'news_praise');
        $praise = intval($praise) - 1;
        return $this->updateByIdWithKV($id, 'news_praise', $praise);
    }

    public function addCommentNumber($id)
    {
        $comment = $this->getFiledById($id, 'activity_comment');
        $comment = intval($comment) + 1;
        return $this->updateByIdWithKV($id, 'activity_comment', $comment);
    }

    public function subCommentNumber($id)
    {
        $comment = $this->getFiledById($id, 'activity_comment');
        $comment = intval($comment) - 1;
        return $this->updateByIdWithKV($id, 'activity_comment', $comment);
    }

}