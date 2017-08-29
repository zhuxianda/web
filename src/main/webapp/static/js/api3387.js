/**
 * Created by chenwl on 11/27/15.
 */
import MD5 from 'spark-md5';

import ChunkedUploader from '../common/ChunkedUploader.js';
import * as Workers from '../workers/Workers.js';
import GameUtils from '../common/GameUtils.js';

var urls = {
    login : "http://mapi.3387.com/client/v1.0/user-login.html",
    logout : "http://mapi.3387.com/client/v1.0/user-logout.html",
    template : "http://mapi.3387.com/client/v1.0/template.html",
    material : "http://mapi.3387.com/client/v1.0/material.html",
    deleteGame :"http://mapi.3387.com/client/v1.0/game-delete.html",
    getGame : "http://mapi.3387.com/client/v1.0/game-info.html",
    getGameList:"http://mapi.3387.com/client/v1.0/game-mine.html",
    checkMaterial : "http://mapi.3387.com/client/v1.0/game-materialCheck.html",
    uploadMaterial : "http://upload.3387.com/client/v1.0/upload.html",
    releaseTemplate : "http://mapi.3387.com/client/v1.1/template-diy.html",
    releaseGame : "http://mapi.3387.com/client/v1.0/game-release.html",
    testGame: "http://www.3387.com/flash/",
    gameURL: "http://mapi.3387.com/client/v1.0/game-test.html",
    feedback: "http://mapi.3387.com/client/v1.0/feedback.html",
    gameLabel:"http://mapi.3387.com/client/v1.0/game-tags.html",
    gameIsUpdate:"http://mapi.3387.com/client/game-meta.html",
    loginConut:"http://stat.m.img4399.com/3387.jpg",
    updateVersionInfo:"http://mapi.3387.com/client/client-webVersion.html",
    gameMessage:"http://mapi.3387.com/client/message-list.html",
    gameMessageStatus:"http://mapi.3387.com/client/message-status.html",
    gameLinkAddress:"http://mapi.3387.com/client/game-getlinks.html",
    limitRelease:"http://mapi.3387.com/client/game-auditRemain.html",
    uploadGameData:"http://mapi.3387.com/client/v1.0/gameData.html",
    uploadGameTrace:"http://mapi.3387.com/client/v1.0/trace-game.html",
    originalMaterial:"http://mapi.3387.com/client/original.html",
    adPosition:"http://mapi.3387.com/client/client-promotiondata.html",
    gameShare:"http://mapi.3387.com/client/message-share.html",
    remainStat:"http://tj.img4399.com:8014/3387_client.jpg",
    materialRedirect:"http://mapi.3387.com/client/material-redirect.html",
    clientStat:"http://mapi.3387.com/client/v1.0/trace-clicks.html",
    clientActivity: 'http://mapi.3387.com/client/huodong.html',
    releaseOptionsUUID:'http://mapi.3387.com/client/trace-option.html',
    originalChange: 'http://mapi.3387.com/client/v1.0/original-change.html',
    releaseTemplateList: "http://mapi.3387.com/client/v1.1/template-mine.html",
    shareTemplateList:'http://mapi.3387.com/client/v1.1/template-list.html',
    shareTemplate: "http://mapi.3387.com/client/v1.0/template-share.html",
    templateInfo: "http://mapi.3387.com/client/v1.1/template-info.html",
    templateHot: "http://mapi.3387.com/client/v1.1/template-hot.html",
    deleteTemplate:"http://mapi.3387.com/client/v1.0/template-delete.html"
};

var sync = {
    syncGameData:"http://mapi.3387.com/client/v1.0/sync-data.html",
    syncGameMeta:"http://mapi.3387.com/client/v1.0/sync-meta.html",
    getSyncGameMeta:"http://mapi.3387.com/client/sync-getMeta.html",
    getSyncGameData:"http://mapi.3387.com/client/sync-getData.html",
    getSyncGameVersion:"http://mapi.3387.com/client/sync-getVersion.html"
};


const fetchJSON = (url, options) => {
    options = Object.assign({credentials: 'include'}, options);
    return fetch(url, options).then(r => r.json());
};

function serializeQuery(params) {
    params['_'] = Date.now();
    return '?' + Object.keys(params).map(key => {
            const val = params[key];
            if (val) {
                return encodeURIComponent(key) + '=' + encodeURIComponent(val)
            }
            else {
                return '';
            }
        }).join('&');
}

export function getOnlineTemplates() {
    return fetchJSON(urls.template).then(json => {
        if (parseInt(json.code) === 100) {
            return Promise.resolve(json.result);
        } else {
            return Promise.reject(json.message);
        }
    });

    return fetch(urls.template, { credentials: 'include' }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            }
            else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getGameList() {
    return fetch(urls.getGameList, { credentials: 'include' }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            }
            else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getGame(uuid) {
    return fetch(urls.getGame + '?uuid=' + uuid, { credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            }
            else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getMaterial(params) {
    return fetch(urls.material + serializeQuery(params), { credentials: 'include' }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            }
            else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function setLogin(params) {
    return fetch(urls.login + serializeQuery(params), { credentials: 'include' }).then(response => {
        return response.json().then(json => {
            return Promise.resolve(json.result);
        });
    });
}

export function setLogout() {
    return fetch(urls.logout, { credentials: 'include' }).then(response => {
        return response.json().then(json => {
            return Promise.resolve(json.result);
        });
    });
}


export function getMessage(params) {
    return fetch(urls.gameMessage + serializeQuery(params), {credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}


export function getMessage(params) {
    return fetch(urls.gameMessage + serializeQuery(params), {credentials: 'include'}).then(r => r.json()).then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
}

export function setMessageRead(params) {
    return fetch(urls.gameMessageStatus + serializeQuery(params), {credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getGameLabels(params){
    return fetch(urls.gameLabel + serializeQuery(params), { credentials: 'include' }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            }
            else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getGameUpdateLog(params){
    return fetch(urls.gameIsUpdate + serializeQuery(params), { credentials: 'include' }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            }
            else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function setFeedBack(params) {
    const formData = new FormData();
    Object.keys(params).forEach(key => formData.append(key, params[key]));
    return fetch(urls.feedback, {method: 'POST', body: formData,credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getLinkURL() {
    return fetch(urls.gameLinkAddress, {credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            }
            else {
                return Promise.reject(json.message);
            }
        });
    });
}


export function getOriginalMaterial(params){
    return fetch(urls.originalMaterial + serializeQuery(params), { credentials: 'include' }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            }
            else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getUpdateVerInfo(){
    return fetch(urls.updateVersionInfo, {credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getReleaseLimitCount(uuid){
    return fetch(urls.limitRelease + '?uuid=' + uuid, { credentials: 'include' }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function filterSameMaterials(md5s){
    const formData = new FormData();
    formData.append('materialMd5', JSON.stringify(md5s));
    return fetch(urls.checkMaterial, { method: 'POST', credentials: 'include' ,body: formData }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function uploadMaterial(md5, blob, name) {
    const formData = new FormData();
    formData.append("md5", md5);
    formData.append("file", blob, name);
    return fetch(urls.uploadMaterial, { method: 'POST', credentials: 'include', body: formData }).then(response => {
        return response.json().then(json => {
            if (json.code == '200') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function uploadGameData(game) {
    return new Promise((resolve, reject) => {
        const uploader = new ChunkedUploader({
            chunckedSize: 1.5 * 1024 * 1024,
            onBeforeUpload: function (infos) {
                console.log('upload before......');
            },
            onUploadSuccess: function (infos) {
                console.log(infos);
                console.log('game data upload success');
                resolve(infos);
            },
            onBeforeUploadChunk: function (infos) {
                console.log('ready upload......');
            },
            onUploadChunkSuccess: function (infos) {
                const { response } = infos;
                const matches = /([0-9]+)-([0-9]+)\/([0-9]+)/.exec(response.result.state);
                if (!matches || isNaN(parseInt(matches[1])) || isNaN(parseInt(matches[2]))) {
                    console.log("Invalid response", response);
                }
                const start = parseInt(matches[1]);
                const end = parseInt(matches[2]);
                console.log('upload success interval start =' + start + ' end =' + end);
            },
            onUploadChunksFailed: function ({response}) {
                reject(response.message);
            }
        });

        // delete uuid timestamp
        const { uuid, timestamp ,...newGame} = game;
        // test game string length more 10M  stringify and toblob need more 300MS
        const blob = new Blob([JSON.stringify(newGame)]);
        Workers.hashBlob(blob).then(md5 => uploader.upload(urls.uploadGameData, blob, md5),reason =>  reject(reason));
    });
}

export function releaseGame(flag, md5, game) {
    const gameMeta = {
        name: game.name,
        cover_url: game.cover.background.url,
        about: game.about,
        word_number: GameUtils.countCharacter(game),
        game_labels: game.gameLabels,
        up_log: game.up_log,
        finish_status: game.finish_status ? 1 : 0
    };
    const formData = new FormData();
    formData.append("uuid", game.uuid);
    formData.append("md5", md5);
    formData.append("flag", flag);
    Object.keys(gameMeta).forEach(key => formData.append(`meta[${key}]`, gameMeta[key]));
    return fetch(urls.releaseGame, { method: 'POST', credentials: 'include', body: formData }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function releaseGameOptionsUuid(uuid, optionIDs) {
    const formData = new FormData();
    formData.append("uuid", uuid);
    optionIDs.forEach((uuid, index) => formData.append(`options[${index}]`, uuid));
    return fetch(urls.releaseOptionsUUID, {method: 'POST', credentials: 'include', body: formData}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function uploadGameUse(game) {
    const gameUse = GameUtils.getGameUse(game);
    const data = {};
    data.diy = gameUse.isUseDiyTemplate ? 1 : 0;
    data.values = gameUse.isUseValue ? 1 : 0;
    data.view = gameUse.isUseView ? 1 : 0;
    const formData = new FormData();
    formData.append("uuid", game.uuid);
    Object.keys(data).forEach(key => formData.append(`data[${key}]`, data[key]));
    return fetch(urls.uploadGameTrace, { method: 'POST', credentials: 'include', body: formData }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getGameTestLink() {
   return urls.testGame;
}

import templates from '../template/templates.js';
export function getOnlineJsonTemplates() {
    return templates;
}

export function getAdPositionData() {
    return fetch(urls.adPosition, {credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getGameShareData(params){
    return fetch(urls.gameShare + serializeQuery(params), { credentials: 'include' }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            }
            else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function sendLoginRemainStat(params) {
    return fetch(urls.remainStat + serializeQuery(params), {credentials: 'include', mode: 'no-cors'});
}

export function getMaterialRedirect(url) {
    return fetch(urls.materialRedirect + serializeQuery({url}), { method: 'GET', credentials: 'include' }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result.url);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function syncGameData(game) {
    return new Promise((resolve, reject) => {
        const uploader = new ChunkedUploader({
            chunckedSize: 1.5 * 1024 * 1024,
            onBeforeUpload: function (infos) {
                console.log('upload before......');
            },
            onUploadSuccess: function (infos) {
                console.log(infos);
                console.log('game data upload success');
                resolve(infos);
            },
            onBeforeUploadChunk: function (infos) {
                console.log('ready upload......');
            },
            onUploadChunkSuccess: function (infos) {
                const { response } = infos;
                const matches = /([0-9]+)-([0-9]+)\/([0-9]+)/.exec(response.result.state);
                if (!matches || isNaN(parseInt(matches[1])) || isNaN(parseInt(matches[2]))) {
                    console.log("Invalid response", response);
                }
                const start = parseInt(matches[1]);
                const end = parseInt(matches[2]);
                console.log('upload success interval start =' + start + ' end =' + end);
            },
            onUploadChunksFailed: function ({response}) {
                reject(response.message);
            }
        });

        // delete uuid timestamp
        const { uuid, timestamp ,...newGame } = game;
        // test game string length more 10M  stringify and toblob need more 300MS
        const blob = new Blob([JSON.stringify(newGame)]);
        Workers.hashBlob(blob).then(
            md5 => {
                if(sync.preGameMD5 !== md5){
                    uploader.upload(sync.syncGameData, blob, md5, {uuid: uuid});
                    sync.preGameMD5 = md5;
                }
                resolve();
            },
            reason =>  reject(reason));
    });
}

export function syncGameMeta(game) {
    if (!UniLogin.getUid()) {
        return;
    }
    const formData = new FormData();
    formData.append("data", [{uuid: game.uuid,name: game.name,cover: game.cover.background.url || ''}]);
    return fetch(sync.syncGameMeta, { method: 'POST', credentials: 'include', body: formData }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function syncGameMeta(game) {
    if (!UniLogin.getUid()) {
        return;
    }
    const formData = new FormData();
    const data = {uuid: game.uuid,name: game.name,cover: game.cover.background.url || ''};
    Object.keys(data).forEach(key => formData.append(`data[0][${key}]`, data[key]));
    return fetch(sync.syncGameMeta, { method: 'POST', credentials: 'include', body: formData }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getSyncGamesMeta(){
    if (!UniLogin.getUid()) {
        return Promise.reject('未登录，请先登录！');
    }
    return fetch(sync.getSyncGameMeta, { method: 'GET', credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(Object.keys(json.result).map(key => json.result[key]));
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getSyncGame(meta){
    return getSyncGameVersion(meta)
        .then(meta => getSyncGameData(meta))
        .then(game => game, reason => null);
}

function getSyncGameVersion(meta){
    return fetch(sync.getSyncGameVersion + serializeQuery({uuid:meta.uuid}), { method: 'GET', credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                if(!Array.isArray(json.result)){
                    return Promise.reject(null);
                }
                json.result.sort((arg1, arg2) => {
                    const time1 = arg1 && arg1.dateline ? arg1.dateline : 0;
                    const time2 = arg2 && arg2.dateline ? arg2.dateline : 0;
                    return time2 - time1;
                });
                return Promise.resolve({id: json.result[0].id, uuid: meta.uuid});
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

function getSyncGameData(meta){
    return fetch(sync.getSyncGameData + serializeQuery({id:meta.id, uuid:meta.uuid}), { method: 'GET', credentials: 'include' }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                json.result.uuid = meta.uuid;
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function sendClientStat(params) {
    const formData = new FormData();
    Object.keys(params).forEach(key => Object.keys(params[key]).forEach(key_ => formData.append(`data[${key}][${key_}]`, params[key][key_])));
    return fetch(urls.clientStat, {method: 'POST' , credentials: 'include', body: formData }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function releaseGameTemplate(game, previewUrl){
    const { template } = game;
    const formData = new FormData();
    const templateStr = JSON.stringify(template);
    formData.append("templateUuid", template.uuid);
    formData.append("md5", MD5.hash(templateStr));
    formData.append("json", templateStr);
    formData.append("meta[name]", template.name);
    formData.append("meta[preview]", previewUrl);
    formData.append("meta[gameUuid]", game.uuid);
    return fetch(urls.releaseTemplate, { method: 'POST', credentials: 'include', body: formData }).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getClientActivityData() {
    return fetch(urls.clientActivity, {method: 'GET', credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getDiyMaterial(params) {
    return fetch(urls.originalChange + serializeQuery(params), {method: 'GET', credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getReleaseTemplateList() {
    return fetch(urls.releaseTemplateList, {method: 'GET', credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getTemplateInfo(params) {
    return fetch(urls.templateInfo + serializeQuery(params), {method: 'GET', credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function getShareTemplateList(params) {
    return fetch(urls.shareTemplateList + serializeQuery(params), {method: 'GET', credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function setShareTemplate(uuid, state) {
    const formData = new FormData();
    formData.append('templateUuid', uuid);
    formData.append('share', state);
    return fetch(urls.shareTemplate, {method: 'POST', credentials: 'include', body: formData}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function addTemplateHot(params) {
    return fetch(urls.templateHot + serializeQuery(params), {method: 'GET', credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function virtualDeleteGame(params){
    return fetch(urls.deleteGame + serializeQuery(params), {method: 'GET', credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}

export function virtualDeleteTemplate(params){
    return fetch(urls.deleteTemplate + serializeQuery(params) , {method: 'GET', credentials: 'include'}).then(response => {
        return response.json().then(json => {
            if (json.code == '100') {
                return Promise.resolve(json.result);
            } else {
                return Promise.reject(json.message);
            }
        });
    });
}