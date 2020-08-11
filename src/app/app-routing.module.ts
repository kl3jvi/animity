import { NgModule } from "@angular/core";
import { PreloadAllModules, RouterModule, Routes } from "@angular/router";

const routes: Routes = [
  {
    path: "",
    loadChildren: () =>
      import("./tabs/tabs.module").then((m) => m.TabsPageModule),
  },
  {
    path: "anime-details",
    loadChildren: () =>
      import("./anime-details/anime-details.module").then(
        (m) => m.AnimeDetailsPageModule
      ),
  },
  {
    path: "videoplayer",
    loadChildren: () =>
      import("./modals/videoplayer/videoplayer.module").then(
        (m) => m.VideoplayerPageModule
      ),
  },
];
@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
