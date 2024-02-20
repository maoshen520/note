

const fileApi = {

    deleteBatch: {
        name: '彻底批量删除',
        url: '/file/delete-batch',
        method: 'post',
        userPower: true,
        dataParam: false,
        successMessage: true
    }


}

export default fileApi;