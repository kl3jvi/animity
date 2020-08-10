import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { VideoplayerPage } from './videoplayer.page';

const routes: Routes = [
  {
    path: '',
    component: VideoplayerPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class VideoplayerPageRoutingModule {}
