import request from '@/utils/request'


// 查询task列表
export function listTasks_(query) {
  return request({
    url: '/task/queryDoneTasks',
    method: 'get',
    params: query
  })
}

// 查询task列表
export function listTask(query) {
  return request({
    url: '/task/list',
    method: 'get',
    params: query
  })
}

// 查询表单
export function formDataShow(taskID) {
  return request({
    url: '/task/formDataShow/'+taskID,
    method: 'get',
  })
}

// 查询表单
export function formDataSave(taskID,data) {
  return request({
    url: '/task/formDataSave/'+taskID,
    method: 'post',
    data:data
  })
}




