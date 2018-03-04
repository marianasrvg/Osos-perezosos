'use strict';

import * as express from 'express';
const router = express.Router();

/* GET home page. */
router.get('/',(req,res,next) => {
  res.render('index', {title: 'Express'});
});

router.get('/test', (req,res,next) => {
  res.send('Hola Mundo');
});

export default router;