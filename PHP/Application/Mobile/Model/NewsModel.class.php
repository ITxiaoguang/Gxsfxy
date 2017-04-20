<?php

namespace Mobile\Model;

use Think\Model;

class NewsModel extends Model
{

    protected $tableName = 'news';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['news_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['news_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['news_id'] = $id;
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

    public function insert($type, $from, $title, $image, $link, $time)
    {
        $data = array();
        $data['news_type'] = $type;
        $data['news_from'] = $from;
        $data['news_title'] = $title;
        $data['news_image'] = $image;
        $data['news_link'] = $link;
        $data['news_comment'] = '0';
        $data['news_praise'] = '0';
        $data['news_click'] = '0';
        $data['news_time'] = $time;
        return $this->add($data);
    }

    //自定义

    public function getInfoByLink($link)
    {
        $where = array();
        $where['news_link'] = $link;
        return $this->where($where)->find();
    }

    public function addClickNumber($id)
    {
        $click = $this->getFiledById($id, 'news_click');
        $click = intval($click) + 1;
        return $this->updateByIdWithKV($id, 'news_click', $click);

    }

    public function subClickNumber($id)
    {
        $click = $this->getFiledById($id, 'news_click');
        $click = intval($click) - 1;
        return $this->updateByIdWithKV($id, 'news_click', $click);

    }

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
        $comment = $this->getFiledById($id, 'news_comment');
        $comment = intval($comment) + 1;
        return $this->updateByIdWithKV($id, 'news_comment', $comment);
    }

    public function subCommentNumber($id)
    {
        $comment = $this->getFiledById($id, 'news_comment');
        $comment = intval($comment) - 1;
        return $this->updateByIdWithKV($id, 'news_comment', $comment);
    }

}