
const directory = {
    name: 'A',
    children: [
        {type: 'file', name: 'B'},
        {type:' directory', name: 'C', children: [
            {type: 'file', name: 'K'},
            {type:' directory', name: 'M', children: []},
        ]},
        {type: 'file', name: 'D'},
        {type:' directory', name: 'E', children: []},
    ],
    type: 'directory'
};


const a = {
    find(fileURL) {

        for()


        if(fileURL.isFile()) {
            console.log("tab"+文件名)
        }else {
            console.log("tab"+文件夹)
            find(文件夹地址);
        }

    }

}
