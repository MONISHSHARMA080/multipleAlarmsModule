export function redirectSystemPath({ path, initial }) {
    console.log("from native intents -->path",path,"---------" ,initial);
    
//   try {
//     if (initial) {
//       // While the parameter is called `path` there is no guarantee that this is a path or a valid URL
//       const url = new URL(path, 'yourappscheme://app.home');
//       // Detection of third-party URLs will change based on the provider
//     //   if (url.hostname === '<third-party-provider-hostname>') {
//     //     return ThirdPartyService.processReferringUrl(url).catch(() => {
//     //       // Something went wrong
//     //       return '/unexpected-error';
//     //     }); 
//       }
//       return path;
//     }
//     return path;
//   } catch {
//     // Do not crash inside this function! Instead you should redirect users
//     // to a custom route to handle unexpected errors, where they are able to report the incident
//     return '/';
//   }
  return path
}
