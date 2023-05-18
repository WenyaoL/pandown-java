import AccountService from "@/api/accountService"


const state = {
  token: '',
  name: '',
  avatar: ''
}

const mutations = {

}

const actions = {
  getSvipAccountNum({ commit }: any,){    
    return AccountService.getSvipAccountNum()
    .then((res:any)=>{
      return res.data
    }).catch(err=>{
      console.log(err);
      return err
    })
  },

  getSvipAccountDetail({ commit }: any,){    
    return AccountService.getSvipAccountDetail()
    .then((res:any)=>{
      return res.data
    }).catch(err=>{
      console.log(err);
      return err
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

