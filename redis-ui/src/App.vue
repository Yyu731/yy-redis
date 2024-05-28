<script setup>
import yyrequest from "@/request/index.js"
import {formatUTC} from "@/util/format.js";
import {reactive, ref} from "vue"

const studentList = ref()
const loading = ref(false)


const getStudentList = () => {
  loading.value = true
  yyrequest.get({
    url: "student"
  }).then(res => {
    studentList.value = res.data
    ElMessage({
      type: 'success',
      message: '获取成功'
    })
    loading.value = false
  })
}

getStudentList()

const type = ref(0);

const handleNewClick = () => {
  type.value = 1;
  resetFormData()
  dialogVisible.value = true;
}

const handleSelectClick = () => {
  type.value = 3;
  for (const key in formData) {
    formData[key] = ""
  }
  dialogVisible.value = true
}

const handleEditClick = (itemData) => {
  type.value = 2;
  for (const key in itemData) {
    formData[key] = itemData[key]
  }
  dialogVisible.value = true;
}

const handleDeleteClick = (itemData) => {
  ElMessageBox.confirm(
      `你确认要删除 <strong>${itemData.name}</strong> 的信息？`,
      '提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true,
        customStyle: {marginBottom: 300 + 'px'}
      }).then(res => {
    yyrequest.delete({
      url: "student/" + itemData.id
    }).then(res => {
      ElMessage({
        type: 'success',
        message: '删除成功'
      })
      getStudentList()
    })
  }).catch(err => {
    ElMessage({
      type: 'warning',
      message: '取消删除',
    })
  })
}

const dialogVisible = ref(false)

const handleSearchClick = () => {
  yyrequest.get({
    url:"student/" + formData.id
  }).then(res => {
    for (const key in res.data) {
      formData[key] = res.data[key]
    }
    dialogVisible.value = true
  })
}

const initFormData = {
  id: "",
  name: "",
  age: 20,
  phone: "",
  email: "",
  birth: "2003-07-31"
}

const formData = reactive({...initFormData})

const handleConfirmClick = () => {
  console.log(formData)
  dialogVisible.value = false
  if (type.value === 1) {
    yyrequest.post({
      url: "student",
      data: formData
    }).then(res => {
      ElMessage({
        type: 'success',
        message: '添加成功'
      })
      getStudentList()
    })
  } else if (type.value === 2) {
    yyrequest.put({
      url: "student",
      data: formData
    }).then(res => {
      ElMessage({
        type: 'success',
        message: '修改成功'
      })
      getStudentList()
    })
  }
}

const resetFormData = () => {
  Object.assign(formData, initFormData)
}

const disabledDate = (data) => {
  return data.getTime() > new Date().getTime()
}

</script>

<template>
  <div class="dialog">
    <el-dialog
        v-model="dialogVisible"
        :title="type === 1 ? '新增学生': '修改学生'"
        center
        :show-close="false"
        width="30%">

      <el-form :model="formData" label-width="auto" size="large">
        <el-form-item class="id" label="id" prop="id">
          <el-input :disabled="type !== 3" v-model="formData.id"></el-input>
          <el-button style="margin-top: 6px;" @click="handleSearchClick">搜索</el-button>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input :disabled="type === 3" v-model="formData.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number :disabled="type === 3" v-model="formData.age" :min="1" :max="100" placeholder="请输入年龄"/>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input :disabled="type === 3" v-model="formData.phone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input :disabled="type === 3" v-model="formData.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="生日" prop="birth">
          <el-date-picker :disabled="type === 3" v-model="formData.birth"
                          :disabled-date="disabledDate"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择生日"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">{{ type === 3 ? '完成': '取消'}}</el-button>
          <el-button type="primary" v-if="type !== 3" @click="handleConfirmClick">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
  <div class="main">
    <div class="content">
      <div class="header">
        <h2 class="title">学生列表</h2>
        <div class="btns">
          <el-button icon="Search" type="success" @click="handleSelectClick">
            查询学生
          </el-button>
          <el-button icon="Plus" type="primary" @click="handleNewClick">
            新增学生
          </el-button>
        </div>

      </div>
      <el-table :data="studentList" stripe style="width: 100%" v-loading="loading">
        <el-table-column
            type="index"
            label="序号"
            align="center"
            width="60"/>
        <el-table-column
            prop="id"
            label="id"
            align="center"
            width="350"/>
        <el-table-column
            prop="name"
            label="姓名"
            align="center"
            width="80"/>
        <el-table-column
            prop="age"
            label="年龄"
            align="center"
            width="80"/>
        <el-table-column
            prop="phone"
            label="电话"
            align="center"
            width="120"/>
        <el-table-column
            prop="email"
            label="邮箱"
            align="center"
            width="180"/>
        <el-table-column
            prop="birth"
            label="生日"
            align="center"
            width="120">
          <template #default="scope">
            {{ scope.row.birth ? formatUTC(scope.row.birth) : '——' }}
          </template>
        </el-table-column>

        <el-table-column
            label="操作"
            align="center"
            width="180">
          <template #default="scope">
            <el-button link type="primary"
                       icon="edit" size="small"
                       @click="handleEditClick(scope.row)">
              编辑
            </el-button>
            <el-button link type="danger"
                       icon="delete" size="small"
                       @click="handleDeleteClick(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>
  </div>
</template>

<style scoped>

.main {
  display: flex;
  justify-content: center;
  align-items: center;

  .content {
    .header {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      margin-top: 50px;

      .title {

      }

      .search {
        align-self: stretch;
      }

      .btns {
        align-self: flex-end;
        margin-bottom: 50px;
      }
    }

    .el-table {
      align-self: center;
    }
  }

  :deep(.el-table__cell) {
    padding: 18px 0px;
  }
}

</style>
