import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { GradesComponent } from './pages/grades/grades.component';
import { StatsComponent } from './pages/stats/stats.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'grades', component: GradesComponent },
    { path: 'stats', component: StatsComponent },
  ];
